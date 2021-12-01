package com.workhub.logman.routing;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private final List<String> resolvedKeys;
    private final List<DataSource> resolvedDataSources;

    public RoutingDataSource() {
        this.resolvedKeys = new ArrayList<>();
        this.resolvedDataSources = new ArrayList<>();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        logger.info("CHOSEN DS KEY : [jdbc/logsaver-shard-0]");
        return "jdbc/logsaver-shard-0";
    }

    @Override
    public void setDataSourceLookup(DataSourceLookup dataSourceLookup) {
        super.setDataSourceLookup(dataSourceLookup);
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
            logger.error("Failed to instantiate datasource: " + e.getMessage(), e);
            throw new DataSourceLookupFailureException("Failed to instantiate some datasources: " + unResolvedDataSources);
        }

        logger.info("Successfully configured" + resolvedDataSources.size() + "datasources. DS Keys: " + resolvedKeys);
        super.setDataSourceLookup(new LocalDataSourceLookUp());
        this.setTargetDataSources(targetDataSources);
        super.setDefaultTargetDataSource(resolvedDataSources.get(0));
        super.afterPropertiesSet();
    }

    public DataSource getInitialisedDataSource(String dsKey) throws ClassNotFoundException {
        if (resolvedKeys.contains(dsKey)) {
            return this.resolveSpecifiedDataSource(dsKey);
        } else {
            /* FIXME: PLACEHOLDER EXCEPTION */
            throw new ClassNotFoundException("");
        }
    }

    private String getJndiFromKey(String key) {
        return "jdbc/logman-shard-" + key;
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
