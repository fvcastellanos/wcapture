package net.cavitos.wcapture.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
public class MySqlDataSourceConfiguration {

    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final String database;

    public MySqlDataSourceConfiguration(@Value("${net.cavitos.datasource.mysql.host:wcapturedb}") final String host,
                                        @Value("${net.cavitos.datasource.mysql.port:3306}") final String port,
                                        @Value("${net.cavitos.datasource.mysql.username:wcapture}") final String username,
                                        @Value("${net.cavitos.datasource.mysql.password:Wc@ptur3$01@}") final String password,
                                        @Value("${net.cavitos.datasource.mysql.database:wcapture}") final String database) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    @Bean
    public DataSource mySqlDataSource(@Value("${spring.datasource.driverClassName:com.mysql.cj.jdbc.MysqlDataSource}") final String driverClassName) {
        final var hikariConfig = new HikariConfig();
        hikariConfig.setDataSourceClassName(driverClassName);
        hikariConfig.addDataSourceProperty("serverName", host);
        hikariConfig.addDataSourceProperty("port", port);
        hikariConfig.addDataSourceProperty("user", username);
        hikariConfig.addDataSourceProperty("password", password);
        hikariConfig.addDataSourceProperty("databaseName", database);
        hikariConfig.addDataSourceProperty("allowPublicKeyRetrieval", "true");
        hikariConfig.addDataSourceProperty("cachePrepStmts", true);
        hikariConfig.addDataSourceProperty("useServerPrepStmts", true);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", 512);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        hikariConfig.addDataSourceProperty("cacheCallableStmts", true);
        hikariConfig.addDataSourceProperty("cacheServerConfiguration", true);
        hikariConfig.addDataSourceProperty("useLocalSessionState", true);
        hikariConfig.addDataSourceProperty("elideSetAutoCommits", true);
        hikariConfig.addDataSourceProperty("alwaysSendSetIsolation", false);
        hikariConfig.addDataSourceProperty("enableQueryTimeouts", false);
        hikariConfig.addDataSourceProperty("zeroDateTimeBehavior", "CONVERT_TO_NULL");
        hikariConfig.addDataSourceProperty("characterEncoding", UTF_8.name());
        hikariConfig.addDataSourceProperty("serverTimezone", "UTC");
        hikariConfig.addDataSourceProperty("rewriteBatchedStatements", true);

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(final DataSource mySqlDataSource) {
        final var sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(mySqlDataSource);

        return sqlSessionFactoryBean;
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(final SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PlatformTransactionManager transactionManager(final DataSource mySqlDataSource) {
        return new DataSourceTransactionManager(mySqlDataSource);
    }

}
