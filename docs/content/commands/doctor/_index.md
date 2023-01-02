+++
title = "Doctor"
chapter = true
weight = 1
pre = "<b>2.2 </b>"
+++
# Doctor
In this chapter we will discuss how to use the doctor command.
This is the primary command that checks all the necessary elements
that might be required for use in a production environment.

This command is used most often for displaying the readiness state of a system.

```bash
probatio doctor
```
When executing this command you can expect the following output below. Please remember that
depending on which applications you have installed and are accessible by the user executing the command.

```bash
Analyzing 100% ?????????????????? 6/6 (0:00:19 / 0:00:00) Check Applications...
Doctor summary (to see all details, run probatio doctor -v):
[?] Docker (Docker version 20.10.17, build 100c701)
[?] Docker Compose (Docker Compose version v2.6.1)
[!] Kubernetes kubectl (kubectl is eather not installed or missing please make sure to install it if it nessary for you)
[?] Internet Access (The open internet was reachable)
[!] Podman (Podman is ether not installed or not running please start the service or install podman)

! Doctor found issues in 2 categorys
```
