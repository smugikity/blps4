    package com.football.configuration;

    import com.atomikos.icatch.jta.UserTransactionManager;
    import com.atomikos.icatch.jta.UserTransactionImp;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.context.annotation.DependsOn;
    import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
    import org.springframework.transaction.annotation.EnableTransactionManagement;
    import org.springframework.transaction.jta.JtaTransactionManager;
    import javax.transaction.TransactionManager;
    import javax.transaction.UserTransaction;

    @Configuration
    @EnableTransactionManagement
    // JpaRepository running in transactional context, config this way to prevent it happen
    // so that we could manually use @Transactional on needed methods only (exclude select/find method to reduce overhead).
    @EnableJpaRepositories(basePackages = "com.football.repository", enableDefaultTransactions = false)
    public class TransactionConfig {
        @Bean(name = "userTransaction")
        public UserTransaction myTransactionImp() throws Throwable {
            UserTransactionImp userTransactionImp = new UserTransactionImp();
    //        userTransactionImp.setTransactionTimeout(10000);
            return userTransactionImp;
        }

        @Bean(name = "atomikosTransactionManager")
        public TransactionManager userTransactionManager() {
            UserTransactionManager userTransactionManager = new UserTransactionManager();
            userTransactionManager.setForceShutdown(true);
            return userTransactionManager;
        }

        @Bean
        @DependsOn({ "userTransaction", "atomikosTransactionManager" })
        public JtaTransactionManager transactionManager() throws Throwable {
            return new JtaTransactionManager(myTransactionImp(), userTransactionManager());
        }
    }