## Spring Boot Application Monitoring through Actuator & Micrometer
Micrometer integration with Prometheus and Grafana

### Overview:
This document explains the integration of spring boot application with actuator & micrometer monitoring. We can show graphical representation of Java heap memory, GC invocation statistics, etc. through Prometheus and Grafana

### Information:

| Description | Github Profile Link  | LinkedIn Profile Link | Email Address
| -------- | -------- | -------- | -------- |
| Vaishali Patil | [<i class="fa fa-external-link"></i>](https://github.com/ravaan-techky/) | [<i class="fa fa-external-link"></i>](https://www.linkedin.com/in/vaishali-patil-4a6679143/) | [ravaan.techky@gmail.com](mailto:ravaan.techky@gmail.com) |

### Description :

The **spring-boot-actuator** module provides all of Spring Boot’s production-ready features.

Actuator endpoints let you monitor and interact with your application. Spring Boot includes a number of built-in endpoints and lets you add your own. For example, the health endpoint provides basic application health information.

Each individual endpoint can be enabled or disabled and exposed (made remotely accessible) over HTTP or JMX. An endpoint is considered to be available when it is both enabled and exposed. The built-in endpoints will only be auto-configured when they are available. Most applications choose exposure via HTTP, where the ID of the endpoint along with a prefix of **/actuator** is mapped to a URL. For example, by default, the health endpoint is mapped to **/actuator/health**.

### Actuator configruation:

By default, all endpoints except for shutdown are enabled. To configure the enablement of an endpoint, use its management.endpoint.<id>.enabled property. The following example enables the shutdown endpoint:

```markdown
management.endpoint.shutdown.enabled=true
```

If you prefer endpoint enablement to be opt-in rather than opt-out, set the management.endpoints.enabled-by-default property to false and use individual endpoint enabled properties to opt back in. The following example enables the info endpoint and disables all other endpoints:

```markdown
management.endpoints.enabled-by-default=false
management.endpoint.info.enabled=true
```

To change which endpoints are exposed, use the following technology-specific include and exclude properties:

| Property | Default |
| --- | --- |
| management.endpoints.jmx.exposure.exclude | |
| management.endpoints.jmx.exposure.include | * |
| management.endpoints.web.exposure.exclude | |
| management.endpoints.web.exposure.include | info, health |

The include property lists the IDs of the endpoints that are exposed. The exclude property lists the IDs of the endpoints that should not be exposed. The exclude property takes precedence over the include property. Both include and exclude properties can be configured with a list of endpoint IDs.

For example, to stop exposing all endpoints over JMX and only expose the health and info endpoints, use the following property:

```markdown
management.endpoints.jmx.exposure.include=health,info
```

'*' can be used to select all endpoints. For example, to expose everything over HTTP except the env and beans endpoints, use the following properties:

```markdown
management.endpoints.web.exposure.include=*
management.endpoints.web.exposure.exclude=env,beans
```
### Grafana dashboard for Spring Boot Application
- [JVM (Micrometer)](https://grafana.com/grafana/dashboards/4701)

<br/><br/>
[<i class="fa fa-arrow-left"></i> **Back**](/documentation/)
