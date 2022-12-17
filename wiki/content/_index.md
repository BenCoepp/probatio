# Probatio
## The all in one container tool
The all in one tool for everyones favoriete container tool
### Chapters
[About Project](#about-project "Goto About Project")

[Installation](#installation "Goto Installation")

[Usage](#usage "Goto Usage")

[Workflow](#workflow "Goto Worklfow")

Please remember the project is currently still in development
so please make sure to not yet use it in production,
and if you do I will not guarantee any issues...

## About Project
[![probatio](https://snapcraft.io/probatio/badge.svg)](https://snapcraft.io/probatio)

## Installation
There are a few ways to install Probatio onto your enviroment
the most common way is through snap
```bash
sudo snap install probatio
```
Snap will download and install probatio and set it up for use. Please remember that for now
it is still under development.
## Usage
```bash
Usage: probatio [-hV] [COMMAND]
The easiest and best way to test any docker application and generate deployments
  -h, --help      display this help message
  -V, --version   display version info

Commands:

The most commonly used probatio commands are:
  analyze  analyze any container project you have
  doctor   check system readiness for use
  init     initialize a new project
  auth     authenticate this application instance
  test     test specific project in a variety of different ways
  monitor  monitor a system and display its information
  help     Display help information about the specified command.

See 'probatio help <command>' to read about a specific subcommand or concept.
```

## Workflow
In the following chapter you will get an overview of the best way to work with probatio. Please note
that this workflow is only explanatory. There are a lot of different ways to use this tool and the one
shown here is also only one of many.