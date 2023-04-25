package org.example;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.lifecycle.Managed;
import org.example.core.service.AuctionService;
import org.example.db.dao.PlayerDao;
import org.example.db.dao.TeamDao;
import org.example.db.entity.Player;
import org.example.db.entity.Team;
import org.example.resources.AuctionResource;
import org.hibernate.SessionFactory;

public class trueApplication extends Application<trueConfiguration> {

    class SessionFactoryManager implements Managed {

        private final SessionFactory sessionFactory;

        public SessionFactoryManager(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
        }

        @Override
        public void start() throws Exception {
            // Perform any setup or resource allocation here
        }

        @Override
        public void stop() throws Exception {
            sessionFactory.close();
        }
    }

    HibernateBundle<trueConfiguration> hibernate = new HibernateBundle<trueConfiguration>(
            Player.class, Team.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(trueConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(final String[] args) throws Exception {
        new trueApplication().run(args);
    }

    @Override
    public String getName() {
        return "true";
    }

    @Override
    public void initialize(final Bootstrap<trueConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(final trueConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application

        SessionFactory sessionFactory = hibernate.getSessionFactory();


        environment.jersey().register(new AuctionResource(new AuctionService(new PlayerDao(sessionFactory), new TeamDao(sessionFactory))));
    }






}