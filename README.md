# README

Aim of the project is to simplify the generation/maintenance of [C4 Model](https://c4model.com/) diagrams.

With this in mind, [Structurizr](https://structurizr.com/)'s approach of diagrams and models as code is a good tool for the diagram generation.

What's found to be missing?

- Easy and fast export of all diagrams
- Generation of additional documentation in a portable format (markdown, in our case)
- Flexibility of the views

## Prerequirements

- Java 17
- Maven

## Build

```
mvn clean package
```

## Run

### Compile and run with maven

```
mvn clean compile exec:java "-Dexec.args=$arg1 $arg2 ..." "-Dexec.mainClass=$mainClass"
```

### Run from the target folder

```
java -cp target/archi-jar-with-dependencies.jar $mainClass $arg1 $arg2 ...
```

### Run the distribution version

```
java -cp archi.jar $mainClass $arg1 $arg2 ...
```

### Run application

```
mvn clean compile exec:java "-Dexec.args=path/to/workspace/directory/ output/" "-Dexec.mainClass=it.vitalegi.archi.App"
```

Will scan directory `path/to/workspace/directory/` and load the configuration from each `.yaml` file found. The output will be available in `./output/` directory.

Both values can be replaced with absolute/relative paths.

## Contributions

Thanks, but no.

## Bugs

Keep them. 

## Support

You are on your own.
