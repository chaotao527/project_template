package com.singhand.sd.template.bizshell.command;

import com.singhand.sd.template.bizshell.service.DatasourceCommandsService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.transaction.annotation.Transactional;

@ShellComponent
public class DatasourceCommands {

  private final DatasourceCommandsService datasourceCommandsService;

  @Autowired
  public DatasourceCommands(DatasourceCommandsService datasourceCommandsService) {

    this.datasourceCommandsService = datasourceCommandsService;
  }

  @ShellMethod(key = "input-datasource", value = "Input data sources", group = "Input resources")
  @SneakyThrows
  @Transactional("bizTransactionManager")
  public void newDatasource(
      @ShellOption(
          value = {"-i", "--input-directory"},
          help = "The directory where input files to place") String inputDirectory) {

  }

}
