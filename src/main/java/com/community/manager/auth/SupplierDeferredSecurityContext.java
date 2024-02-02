package com.community.manager.auth;

import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

import java.util.function.Supplier;

public class SupplierDeferredSecurityContext implements DeferredSecurityContext {
    private final Supplier<SecurityContext> supplier;
    private boolean missingContext;
    private SecurityContext securityContext;
    private final SecurityContextHolderStrategy strategy;

    SupplierDeferredSecurityContext(Supplier<SecurityContext> supplier,
                                    SecurityContextHolderStrategy strategy) {
        this.supplier = supplier;
        this.strategy = strategy;
        this.missingContext = false;
    }

    /**
     * 获取
     * @return 上下文
     */
    @Override
    public SecurityContext get() {
        init();
        return this.securityContext;
    }

    /**
     * 是否生成
     * @return 是否生成
     */
    @Override
    public boolean isGenerated() {
        init();
        return this.missingContext;
    }

    /**
     * 初始化
     */
    private void init() {
        if (this.securityContext != null) {
            return;
        }
        this.securityContext = this.supplier.get();
        if (this.securityContext == null) {
            this.missingContext = true;
            this.securityContext = this.strategy.createEmptyContext();
        }
    }
}
