package org.superbiz.moviefun;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by aw169 on 12/13/17.
 */
@Configuration
public class DbConfig {



    @Bean
    public static HibernateJpaVendorAdapter jpaAdapter(){

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabasePlatform("MYSQL");
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        adapter.setGenerateDdl(true);

        return adapter;
    }


    public static DataSource buildDataSource(String url, String username, String password){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        HikariConfig config = new HikariConfig();
        config.setDataSource(dataSource);

        return new HikariDataSource(config);
    }

    public static LocalContainerEntityManagerFactoryBean buildLocalEntityManagerBean(DataSource dataSource, HibernateJpaVendorAdapter adapter, String unitName){
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaVendorAdapter(adapter);
        factoryBean.setPackagesToScan(DbConfig.class.getPackage().getName());
        factoryBean.setPersistenceUnitName(unitName);

        return factoryBean;
    }

    @Configuration
    public static class MoviesDBConfig{

        @Bean
      //  @Qualifier("moviesDataSource")
        public DataSource moviesDataSource(
            @Value("${moviefun.datasources.movies.url}") String url,
            @Value("${moviefun.datasources.movies.username}") String username,
            @Value("${moviefun.datasources.movies.password}") String password
        ) {
           return buildDataSource(url, username, password);
        }

        @Bean
        @Qualifier("movies")
        public LocalContainerEntityManagerFactoryBean moviesEntityManager(/*@Qualifier("moviesDataSource")*/ DataSource moviesDataSource, HibernateJpaVendorAdapter jpaAdapter){

            return buildLocalEntityManagerBean(moviesDataSource, jpaAdapter, "movies");
        }

        @Bean
       // @Qualifier("moviesTM")
        public PlatformTransactionManager moviesTM(@Qualifier("movies") LocalContainerEntityManagerFactoryBean moviesEntityManager){

            PlatformTransactionManager manager = new JpaTransactionManager(moviesEntityManager.getObject());
            return manager;
        }


    }

    @Configuration
    public static class AlbumsDBConfig{

        @Bean
        //@Qualifier("albumsDataSource")
        public DataSource albumsDataSource(
            @Value("${moviefun.datasources.albums.url}") String url,
            @Value("${moviefun.datasources.albums.username}") String username,
            @Value("${moviefun.datasources.albums.password}") String password
        ) {
            return buildDataSource(url, username, password);
        }



        @Bean
        @Qualifier("albums")
        public LocalContainerEntityManagerFactoryBean albumsEntityManager(/*@Qualifier("albumsDataSource")*/ DataSource albumsDataSource, HibernateJpaVendorAdapter jpaAdapter){
            return buildLocalEntityManagerBean(albumsDataSource, jpaAdapter, "albums");
        }

        @Bean
       // @Qualifier("albumsTM")
        public PlatformTransactionManager albumsTM(@Qualifier("albums") LocalContainerEntityManagerFactoryBean albumsEntityManager){

            PlatformTransactionManager manager = new JpaTransactionManager(albumsEntityManager.getObject());
            return manager;
        }

    }


}
