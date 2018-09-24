package net.cavitos.wcapture.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"net.cavitos.wcapture.repositories"})
@EnableTransactionManagement
public class MySqlDataSourceConfiguration {

    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final String database;

    public MySqlDataSourceConfiguration(@Value("${net.cavitos.datasource.mysql.host:mysql-host}") final String host,
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
    public DataSource mySqlDataSource(@Value("${spring.datasource.driverClassName:com.mysql.jdbc.Driver}") final String driverClassName) {
        final var jdbcUrl = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false";
        final var hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(this.username);
        hikariConfig.setPassword(this.password);

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(final DataSource mySqlDataSource) {
        final var sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(mySqlDataSource);

        final PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setConfigLocation(pathMatchingResourcePatternResolver.getResource("classpath:/mybatis/configuration/Configuration.xml"));

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
