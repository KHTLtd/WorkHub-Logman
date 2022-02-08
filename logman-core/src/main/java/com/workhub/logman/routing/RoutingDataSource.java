package com.workhub.logman.routing;

import com.workhub.logman.exceptions.LogmanDatasourceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Router class for dynamic
 * management of multiple data sources
 *
 * @since 1.0.0 | 03.12.2021
 * @author alexkirillov
 */
@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

    private final List<String> resolvedKeys;
    private final List<DataSource> resolvedDataSources;

    public RoutingDataSource() {
        this.resolvedKeys = new ArrayList<>();
        this.resolvedDataSources = new ArrayList<>();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String dsKey = System.getProperty("logman.db.key", "0");
        String dsJndi = getJndiFromKey(dsKey);
        if (!resolvedKeys.contains(dsJndi)) {
            log.error("Resolved DS keys list does not contain key [" + dsJndi + "]. Connection will be routed to the default DS.");
        }
        return dsJndi;
    }

    @Override
    public void setDataSourceLookup(DataSourceLookup dataSourceLookup) {
        super.setDataSourceLookup(new LocalDataSourceLookUp());
    }

    @Override
    public void afterPropertiesSet() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        List<String> unResolvedDataSources = new ArrayList<>();
        String jndi = "";
        try {
            jndi = getJndiFromKey(0 + "");
            DataSource ds = this.resolveSpecifiedDataSource(jndi);
            targetDataSources.put(jndi, ds);
            resolvedDataSources.add(ds);
            resolvedKeys.add(jndi);
        } catch (Exception e) {
            unResolvedDataSources.add(jndi);
            log.error("Failed to instantiate datasource: " + e.getMessage(), e);
            throw new DataSourceLookupFailureException("Failed to instantiate some datasources: " + unResolvedDataSources);
        }

        log.info("Successfully configured {} datasources. DS Keys: {}", resolvedDataSources.size(), resolvedKeys);
        super.setDataSourceLookup(new LocalDataSourceLookUp());
        this.setTargetDataSources(targetDataSources);
        super.setDefaultTargetDataSource(resolvedDataSources.get(0));
        super.afterPropertiesSet();
    }

    public DataSource getInitialisedDataSource(String dsKey) throws LogmanDatasourceException {
        if (resolvedKeys.contains(dsKey)) {
            return this.resolveSpecifiedDataSource(dsKey);
        } else {
            /* FIXME: PLACEHOLDER EXCEPTION */
            throw new LogmanDatasourceException("The resolved keys list does not contain "
                    + "the passed value : [{" + dsKey + "}]");
        }
    }

    private String getJndiFromKey(String key) {
        return "jdbc/logman-shard-" + key;
    }

    /**
     * Get DB URLs of resolved DS's
     *
     * @return pathList
     */
    private List<String> getResolvedDataSourcePaths() {
        List<String> pathList = new ArrayList<>();
        for (DataSource ds : resolvedDataSources) {
            try (Connection connection = ds.getConnection()) {
                String path = connection.getMetaData().getURL();
                pathList.add(path);
            } catch (SQLException e) {
                log.error("Failed to get connection to a resolved DS [" + ds + "] : " + e.getMessage(), e);
            }
        }
        return pathList;
    }

    private class LocalDataSourceLookUp implements DataSourceLookup {

        @Override
        public DataSource getDataSource(String dataSourceName) throws DataSourceLookupFailureException {
            JndiObjectFactoryBean jofb = new JndiObjectFactoryBean();
            jofb.setCache(true);
            jofb.setResourceRef(true);
            jofb.setLookupOnStartup(true);
            jofb.setExpectedType(DataSource.class);
            jofb.setJndiName(dataSourceName);

            return (DataSource) jofb.getObject();
        }
    }

}
