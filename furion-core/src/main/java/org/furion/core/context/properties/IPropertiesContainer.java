package org.furion.core.context.properties;

import java.util.List;

/**
 * 配置对象 容器规范
 */
public interface IPropertiesContainer {

    /**
     * 接收 具体的 配置项更新事件，更新自身。
     */
    default void refresh(List<IPropertyValueChangeEvent> refreshDataList) {
    }

}
