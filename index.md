## Spring Boot Application Monitoring through Actuator & Micrometer
Micrometer integration with Prometheus and Grafana

### Overview:
This document explains the integration of spring boot application with actuator & micrometer monitoring. We can show graphical representation of Java heap memory, GC invocation statistics, etc. through Prometheus and Grafana

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

### Prometheus configuration:

```markdown
# my global config
global:
  scrape_interval:     15s # Set the scrape interval to every 15 seconds. Default is every 1 minute.
  evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute.
  # scrape_timeout is set to the global default (10s).

# Alertmanager configuration
alerting:
  alertmanagers:
  - static_configs:
    - targets:
      # - alertmanager:9093

# Load rules once and periodically evaluate them according to the global 'evaluation_interval'.
rule_files:
  # - "first_rules.yml"
  # - "second_rules.yml"

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
scrape_configs:
  # The job name is spring boot application name.
  - job_name: 'spring-application'
    metrics_path: '/application/actuator/prometheus'
    scrape_interval: 5s
    static_configs:
    - targets: ['localhost:8080']
```
### pom.xml configuration:

- Add below mentioned depenedecy for actuator
```markdown
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-actuator</artifactId>
	</dependency>
```
- Add below mentioned depenedecy for micrometer

```markdown
	<dependency>
		<groupId>io.micrometer</groupId>
		<artifactId>micrometer-registry-prometheus</artifactId>
	</dependency>
```
- Add below mentioned depenedecy for micrometerJVM attributes

```markdown
	<dependency>
		<groupId>io.github.mweirauch</groupId>
		<artifactId>micrometer-jvm-extras</artifactId>
		<version>0.1.3</version>
	</dependency>
``` 
**Note:** micrometer-jvm-extras artifact version is taken from maven repository. Please check for latest one.

### Prometheus Bean configuration:
- Register MeterRegistery of Micrometer
```java
	/**
	 * Configurer.
	 *
	 * @param applicationName the application name
	 * @return the meter registry customizer
	 */
	@Bean
	MeterRegistryCustomizer<MeterRegistry> configurer(
			@Value("${spring.application.name}") final String applicationName) {
		return (registry) -> registry.config().commonTags("application", applicationName);
	}
```
**Note:** To register above mentioned bean, we need micrometer-registry-prometheus dependency.

-- Create bean of MemoryMetrics and ThreadMetrics
```java
	/**
	 * Process memory metrics.
	 *
	 * @return the meter binder
	 */
	@Bean
	public MeterBinder processMemoryMetrics() {
		return new ProcessMemoryMetrics();
	}

	/**
	 * Process thread metrics.
	 *
	 * @return the meter binder
	 */
	@Bean
	public MeterBinder processThreadMetrics() {
		return new ProcessThreadMetrics();
	}
```
**Note:** To create above mentioned bean, we need micrometer-jvm-extras dependency.

### Prometheus
- You can download prometheus from - [Here](https://github-production-release-asset-2e65be.s3.amazonaws.com/6838921/9cf6e100-aa67-11ea-90b1-6244edd666b0?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A%2F20200614%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20200614T170952Z&X-Amz-Expires=300&X-Amz-Signature=fdb503abf85f0a3d7212d3361ec4f47e6e14652ab028bac14de5aa73e3712ab8&X-Amz-SignedHeaders=host&actor_id=0&repo_id=6838921&response-content-disposition=attachment%3B%20filename%3Dprometheus-2.19.0.windows-amd64.tar.gz&response-content-type=application%2Foctet-stream) **Note:** For linux version OR Docker version please visit - [Prometheus site](https://prometheus.io/download/)
- Extract prometheus-2.19.0.windows-amd64.tar.gz to **prometheus-2.19.0**
- Replace prometheus.yml configuration file from **$(project_directory)/src/main/resources/prometheus.yml** to **prometheus-2.19.0/bin/** folder.
- Start prometheus query executor from using **prometheus-2.19.0/bin/prometheus.exe**
- Launch http://localhost:9090/graph from browser.

| <i class="fa fa-picture-o"></i> | <i class="fa fa-picture-o"></i> | <i class="fa fa-picture-o"></i> |
  | -------- | -------- | -------- |
  | CPU Usage<br/><br/><a href="images/prometheus_cpu_usage.jpg">![Overview](images/prometheus_cpu_usage_s.jpg)<a/> |  Logback Usage<br/><br/><a href="images/prometheus_logback_usage.jpg">![Overview](images/prometheus_logback_usage_s.jpg)<a/> | Memory Swap Usage<br/><br/><a href="images/prometheus_memory_swap.jpg">![Overview](images/prometheus_memory_swap_s.jpg)</a> |

### Grafana
- Download Grafana from - [Here](https://dl.grafana.com/oss/release/grafana-7.0.3.windows-amd64.zip)  **Note:** For linux version OR Docker version please visit - [Grafana site](https://grafana.com/grafana/download)
- Extract grafana-7.0.3.windows-amd64.zip to **grafana-7.0.3**
- Start grafana from using **grafana-7.0.3\bin\grafana-server.exe**
- Launch http://localhost:3000/ from browser. Default username and password is admin / admin for grafana.
![Grafana_Login](images/grafana_login.jpg)

### Grafana configuration:

- **Data source**
	- Add your first data source.
	![grafana_add_datasource](images/grafana_add_datasource.jpg)
	- Select prometheus data source from list of data-sources.
	![grafana_select_prometheus_datasource](images/grafana_select_prometheus_datasource.jpg)
	- Add prometheus data source details.
	![grafana_add_details_prometheus_datasource](images/grafana_add_details_prometheus_datasource.jpg)
	- Test & Save data source.

- **Dashboard**
	- Import dashboard with (+) sign
	![grafana_import_dashboard](images/grafana_import_dashboard.jpg)
	- Import [JVM (Micrometer)](https://grafana.com/grafana/dashboards/4701) dashboard by 'Import Via Grafana.com'
	![grafana_import_4701_dashboard](images/grafana_import_4701_dashboard.jpg)
	- Edit dashboard name & datasource.
	![grafana_import_4701_and_save_dashboard](images/grafana_import_4701_and_save_dashboard.jpg)
	- Save dashboard

### Post Owner Information:

| Description | Github Profile Link  | LinkedIn Profile Link | Email Address
| -------- | -------- | -------- | -------- |
| Vaishali Patil | [<i class="fa fa-external-link"></i>](https://github.com/ravaan-techky/) | [<i class="fa fa-external-link"></i>](https://www.linkedin.com/in/vaishali-patil-4a6679143/) | [ravaan.techky@gmail.com](mailto:ravaan.techky@gmail.com) |

<br/><br/>
[<i class="fa fa-arrow-left"></i> **Back**](/documentation/)
