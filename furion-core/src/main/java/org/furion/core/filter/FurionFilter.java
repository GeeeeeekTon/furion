package org.furion.core.filter;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.furion.core.exception.FilterExclusiveThreadLocalReleasedException;
import org.furion.core.exception.FurionException;

public abstract class FurionFilter implements IFurionFilter, Comparable<FurionFilter> {

    private transient final ThreadLocal<FilterExclusiveOwner> filterExclusiveOwnerThreadLocal = new ThreadLocal<>();

    public FurionFilter() {
    }

    protected ChannelFuture writeAndFlush(Object o) {
        ChannelFuture future = getCurrentExclusiveOwnerChannel().writeAndFlush(o);
        future.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    setCurrentExclusiveOwnerWhetherWriteAndFlush(true);
                    releaseCurrentThreadLocal();
                } else {
                    System.out.println("回写客户端失败");
                }
            }
        });
        return future;
        //ReferenceCountUtil.release(o);
    }


    public abstract String filterType();


    public abstract int filterOrder();

    public boolean isStaticFilter() {
        return true;
    }

    public boolean isFilterDisabled() {
        return false;
    }

    protected Channel getCurrentExclusiveOwnerChannel() {
        try {
            Channel channel = filterExclusiveOwnerThreadLocal.get().getExclusiveOwnerChannel();
            if (channel == null) {
                throw new FilterExclusiveThreadLocalReleasedException("currentExclusiveOwnerChannel");
            }
            return channel;
        } catch (Exception e) {

        }
        throw new FilterExclusiveThreadLocalReleasedException("currentExclusiveOwnerChannel");
    }

    protected void setCurrentExclusiveOwnerWhetherWriteAndFlush(Boolean var) {
        FilterExclusiveOwner filterExclusiveOwner = filterExclusiveOwnerThreadLocal.get();
        filterExclusiveOwner.setWhetherWriteAndFlush(var);
        filterExclusiveOwnerThreadLocal.remove();
        filterExclusiveOwnerThreadLocal.set(filterExclusiveOwner);
    }

    protected Boolean getCurrentExclusiveOwnerWhetherWriteAndFlush() {
        return filterExclusiveOwnerThreadLocal.get() != null && filterExclusiveOwnerThreadLocal.get().isWhetherWriteAndFlush();
    }

    protected Long getCurrentExclusiveOwnerRequestId() {
        return filterExclusiveOwnerThreadLocal.get().getExclusiveOwnerRequest();
    }

    protected FurionFilter init(Long id, Channel channel) {
        filterExclusiveOwnerThreadLocal.set(new FilterExclusiveOwner(false, id, channel));
//        ownerConcurrentHashMap.put(id, );
        return this;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }


    protected boolean shouldFilter0() {
        if (filterType().equalsIgnoreCase(FilterType.PRE.name())) {
            return shouldFilter() && !getCurrentExclusiveOwnerWhetherWriteAndFlush();
        }
        if (filterType().equalsIgnoreCase(FilterType.POST.name())) {
            return shouldFilter();
        }
        return true;
    }

    @Override
    public Object run() throws FurionException {
        return null;
    }


//    public FurionFilterResult runFilter() {
//        FurionFilterResult zr = new FurionFilterResult();
//        if (!this.isFilterDisabled()) {
//            if (this.shouldFilter()) {
////                Tracer t = TracerFactory.instance().startMicroTracer("ZUUL::" + this.getClass().getSimpleName());
//
//                try {
//                    Object res = this.run();
//                    zr = new FurionFilterResult(res, FurionException.Status.SUCCESS);
//                } catch (Throwable var7) {
////                    t.setName("ZUUL::" + this.getClass().getSimpleName() + " failed");
//                    zr = new FurionFilterResult(FurionException.Status.FAILED);
//                    zr.setException(var7);
//                } finally {
////                    t.stopAndLog();
//                }
//            } else {
//                zr = new FurionFilterResult(FurionException.Status.SKIPPED);
//            }
//        }
//
//        return zr;
//    }

    public int compareTo(FurionFilter filter) {
        return Integer.compare(this.filterOrder(), filter.filterOrder());
    }


    protected void releaseCurrentThreadLocal() {
        filterExclusiveOwnerThreadLocal.remove();
    }

}
