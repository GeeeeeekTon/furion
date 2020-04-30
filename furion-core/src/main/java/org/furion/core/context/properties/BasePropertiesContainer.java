package org.furion.core.context.properties;

public abstract class BasePropertiesContainer implements IPropertiesContainer {

    public BasePropertiesContainer() {
        PropertiesManager propertiesManager = PropertiesManager.getInstance();
        propertiesManager.register(this);
    }

    @Override
    public void register(PropertiesManager propertiesRepository) {
        propertiesRepository.register(this);
    }
}
