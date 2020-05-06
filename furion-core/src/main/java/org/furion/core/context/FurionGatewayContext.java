package org.furion.core.context;

import org.furion.core.context.properties.PropertiesManager;
import org.furion.core.filter.FurionFilter;
import org.furion.core.filter.FurionFilterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FurionGatewayContext implements GatewayContext {

    private static final Logger LOG = LoggerFactory.getLogger(FurionGatewayContext.class);
    private PropertiesManager propertiesManager;
    private FurionProperties furionProperties;

    private static FurionFilterRegistry registry;


    public static FurionFilterRegistry getRegistry() {
        return registry;
    }

    public static void setRegistry(FurionFilterRegistry registry) {
        FurionGatewayContext.registry = registry;
    }

    public FurionGatewayContext(String... args) {
        propertiesManager = PropertiesManager.getInstance();
        furionProperties = new FurionProperties();
        init();
        refresh();
    }

    public static void main(String[] args) {
        System.out.println(FurionGatewayContext.class.getResource("/META-INF").getPath());
    }

    private void init() {

    }

    /**
     * 加载Filter
     */
    public void refresh() {


    }


//    @Override
//    public Set<FurionFilter> getFilterSetByType(FilterType filterType) {
//        if (FilterType.POST == filterType) {
//            return postFilters;
//        } else if (FilterType.PRE == filterType) {
//            return preFilters;
//        }
//        throw new RuntimeException("invalid filterType:" + filterType);
//
//    }

    @Override
    public void addFilter(FurionFilter filter) {
        registry.registerFilter(filter);
    }
}
