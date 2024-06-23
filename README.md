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

## File structure

```
elements:
- type: PERSON | SOFTWARE_SYSTEM | CONTAINER | COMPONENT | GROUP | DEPLOYMENT_ENVIRONMENT | DEPLOYMENT_NODE | CONTAINER_INSTANCE | SOFTWARE_SYSTEM_INSTANCE | INFRASTRUCTURE_NODE
  id: id of the element, can be skipped
  parentId: used only on elements on top level
  name: free text
  description: free text
  tags:
  - tag 1
  - tag 2
  - ...
  technologies:
  - tech 1
  - tech 2
  - ...
  properties:
    free text 1: free text
    free text 2: free text
    free text 3: free text
  containerId: if type=CONTAINER_INSTANCE, id of the container
  softwareSystemId: if type=SOFTWARE_SYSTEM_INSTANCE, id of the software system
  elements:
  - list of children elements, can be nested
relations:
- id: id of the relation, can be skipped
  from: id of the element from whom the relation starts
  to: id of the element to whom the relation ends
  description: free text
  label: free text
  sprite: 
  link: link to be used on the relation
  tags:
  - tag 1
  - tag 2
  - ...
  technologies:
  - tech 1
  - tech 2
  - ...
  properties:
    free text 1: free text
    free text 2: free text
    free text 3: free text
options:
  direction: LEFT_TO_RIGHT | TOP_TO_BOTTOM
  inheritRelations: true | false
  hideRelationsText: true | false
  hiddenRelations:
  - id: hidden relation id
    elements:
    - element id 1
    - element id 2
    - ...
style:
  skinParams:
  - key: skin param key
    value: skin param value
  - ...
diagrams:
- type: LANDSCAPE | SYSTEM_CONTEXT | DEPLOYMENT | FLOW
  name: unique name of the diagram, used as filename during export
  title: title of the diagram
  options: same structure as top-level <options>
  style: same structure as top-level <style>
flows:
- id: unique id of the flow, used as filename during export
  name: title of the flow
  steps: same structure as top-level <relations>
```

### Diagrams

#### Landscape Diagram

<https://c4model.com/#SystemLandscapeDiagram>

```
- type: LANDSCAPE
  ...
```

#### System Context Diagram

<https://c4model.com/#ContainerDiagram>

```
- type: SYSTEM_CONTEXT
  target: ID of the target Software System
  ...
```

#### Deployment Diagram

<https://c4model.com/#DeploymentDiagram>

```
- type: DEPLOYMENT
  scope: * | softwareSystemId
  environment: ID of the target DeploymentEnvironment
  ...
```

#### Flow Diagram

<https://c4model.com/#DynamicDiagram>

```
- type: FLOW
  flow: ID of the target flow
  ...
```

## Contributions

Thanks, but no.

## Bugs

Keep them. 

## Support

You are on your own.
