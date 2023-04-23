package org.example;

import io.dropwizard.core.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.*;
import jakarta.validation.constraints.*;

import javax.xml.crypto.Data;

public class trueConfiguration extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
    public  void setDataSourceFactory(DataSourceFactory dataSourceFactory){
        this.database = dataSourceFactory;
    }

    // TODO: implement service configuration
}
