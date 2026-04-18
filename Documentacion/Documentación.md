
## Problema que se quiere resolver
El problema a resolver es desarrollar un sistema de software que permita optimizar y modernizar la gestión académica de los estudiantes 

## Usuarios del sistema
###### **Personal de la oficina de alumnos:** 
tienen rol de administrador. Gestionan la información académica. 
###### **Estudiantes:** 
consultan información personal, correlatividades, materias aprobadas, etc. - 
###### **Profesores:** 
cargan notas o consultar el listado de alumnos.

## Funcionalidades principales
Las principales funcionalidades del sistema son las siguientes:
- Gestionar Estudiante.
	- CRUD
- Gestionar Docente. 
	- CRUD
- Gestionar Materia. 
	- CRUD
- Gestionar Carrera. 
	- CRUD
- Asociar alumno a Carreras.
- Permitir al Estudiante anotarse a una Materia.
- Gestionar Plan De Estudio. 
	- CRUD
- Permitir visualizar el progreso de cada estudiante. 
	- Progreso de las materias
	- porcentaje de materias aprobadas respecto al plan de estudio


## Restricciones técnicas
Mantenible. 
Interfaz amigable. 
Tiempo de respuesta rápida. 
Capacidad de tener accesos concurrentes. 
Base de datos capaz de soportar muchos usuarios registrados. 
Protección de datos (estudiantes y docentes). 
Alta disponibilidad. 
Portabilidad.

## Tamaño del equipo
El equipo esta consolidado por 3 personas.

## Tecnologías elegidas
###### Las tecnologías usadas son las siguientes:
- Java = lenguaje de programación
- Apache Maven = Gestión empaquetado y compilación
- JavaSpark = Micro framework 
- SQlite = Bases de datos
- ActiveJDBC = ORM
- Mustache = Vistas
- Git y Github = Versionado y seguimiento de código

Elegimos estas tecnologías por que son herramientas ya conocidas de la carrera y que tenemos experiencia en ellas.

## Plazo estimado
El plazo estimado del proyecto son de 3 meses debido a la duración de la materia en este cuatrimestre y llevaremos el proyecto junto a la par de la materia.

## Cambios de alcance ocurridos


## Problemas encontrados

## Forma de organización del equipo
Usaremos una metodología ágil en este caso scrum en donde nos dividiremos las funcionalidades y haremos meeting para ver como progresa cada uno y ayudarnos mutuamente como también a lo largo del proyecto nos turnaremos para ser el scrum master.



## Análisis de riesgos
a):

| Tipo           | Descripción y mitigación sugerida                                                                                                                                                                                               | Probabilidad | Impacto |
| -------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------ | ------- |
| Técnico        | Curva de aprendizaje en JavaSpark, ActiveJDBC y Mustache — tecnologías con comunidad limitada y documentación escasa.<br><br>Mitigación: reservar sprints iniciales para picos técnicos y prototipos.                           | Media        | Alto    |
| Técnico        | SQLite no soporta accesos concurrentes robustos; puede ser un cuello de botella con múltiples usuarios simultáneos.<br><br>Mitigación: evaluar migración a PostgreSQL o H2 si la carga supera el prototipo.                     | Media        | Crítico |
| Técnico        | Lógica compleja de correlatividades e inscripciones puede generar bugs difíciles de detectar en producción.<br><br>Mitigación: cobertura de tests unitarios para reglas de negocio críticas.                                    | Alta         | Alto    |
| Técnico        | Protección insuficiente de datos sensibles (legajo, notas, datos personales) ante vulnerabilidades web básicas.<br><br>Mitigación: validar inputs, usar HTTPS y aplicar control de acceso por rol (RBAC).                       | Media        | Crítico |
| Organizacional | Desalineación en la prioridad de tareas entre los 3 integrantes, sin un backlog formal ni criterios de aceptación claros.<br><br>Mitigación: definir Definition of Done y revisar el backlog en cada sprint planning.           | Baja         | Medio   |
| Organizacional | Falta de integración continua y proceso de code review; cambios no integrados pueden generar conflictos en el repositorio.<br><br>Mitigación: establecer workflow de Git con pull requests y revisión obligatoria.              | Media        | Medio   |
| Organizacional | Ausencia de entorno de pruebas (staging); cambios llegan directamente a producción sin validación intermedia.<br><br>Mitigación: crear branch de staging y un entorno de despliegue separado.                                   | Alta         | Alto    |
| Planificación  | Desviación en la estimación de historias complejas (correlatividades, detección de riesgo de abandono) sin puntos de referencia previos.<br><br>Mitigación: usar Planning Poker y revisar velocidad del equipo sprint a sprint. | Alta         | Crítico |
| Planificación  | Plazo fijo de 3 meses con alcance ambicioso (12+ funcionalidades); riesgo de scope creep o entrega incompleta.<br><br>Mitigación: priorizar MVP con funcionalidades core y dejar opcionales para iteraciones posteriores.       | Alta         | Alto    |
| Planificación  | Sección 'Cambios de alcance' vacía: indica falta de proceso formal para gestionar cambios de requerimientos.<br><br>Mitigación: documentar todo cambio de alcance y re-estimar impacto en el sprint.                            | Media        | Medio   |
| Humano         | Falta de disponibilidad de un rol clave (un miembro del equipo de 3) por exámenes u obligaciones académicas.<br><br>Mitigación: cross-training y documentar cada módulo para que otro integrante pueda continuar.               | Baja         | Crítico |
| Humano         | Conocimiento concentrado: si el experto de un módulo no está disponible, el equipo no puede avanzar ni dar soporte.<br><br>Mitigación: pair programming y documentación técnica mínima por módulo.                              | Media        | Alto    |
b):

| Tipo           | Descripción y mitigación sugerida                                                                                                  | Probabilidad | Impacto    |
| -------------- | ---------------------------------------------------------------------------------------------------------------------------------- | ------------ | ---------- |
| Técnico        | Aunque el equipo tenga experiencias en las tecnologías algunas tuvimos usándolas poco tiempo como Mustache, JavaSpark, ActiveJDBC. | Alta         | Crítico    |
| Planificación  | Es un proyecto muy grande dado que son muchas funcionalidades y somos 3 personas en el equipo                                      | Media        | Alto       |
| Organizacional | Conflictos entre integrantes del equipo a trabajar los mismos módulos a la vez                                                     | Alta         | Bajo-medio |
| Humano         | Falta de disponibilidad de alguno del equipo debido a factores externos podría atrasar el proyecto                                 | Alta         | Alto       |
| Humano         | Podría a ver conflictos entre los liderazgos al cambiar los scrum master dado que cada persona puede dirigir de diferentes maneras | Baja         | Medio      |
C):
Estos son los riegos que encontró la IA y el equipo no

| Tipo           | Descripción y mitigación sugerida                                                                                                                                                                                               | Probabilidad | Impacto |
| -------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------ | ------- |
| Técnico        | SQLite no soporta accesos concurrentes robustos; puede ser un cuello de botella con múltiples usuarios simultáneos.<br><br>Mitigación: evaluar migración a PostgreSQL o H2 si la carga supera el prototipo.                     | Media        | Crítico |
| Técnico        | Lógica compleja de correlatividades e inscripciones puede generar bugs difíciles de detectar en producción.<br><br>Mitigación: cobertura de tests unitarios para reglas de negocio críticas.                                    | Alta         | Alto    |
| Técnico        | Protección insuficiente de datos sensibles (legajo, notas, datos personales) ante vulnerabilidades web básicas.<br><br>Mitigación: validar inputs, usar HTTPS y aplicar control de acceso por rol (RBAC).                       | Media        | Crítico |
| Organizacional | Desalineación en la prioridad de tareas entre los 3 integrantes, sin un backlog formal ni criterios de aceptación claros.<br><br>Mitigación: definir Definition of Done y revisar el backlog en cada sprint planning.           | Baja         | Medio   |
| Organizacional | Falta de integración continua y proceso de code review; cambios no integrados pueden generar conflictos en el repositorio.<br><br>Mitigación: establecer workflow de Git con pull requests y revisión obligatoria.              | Media        | Medio   |
| Organizacional | Ausencia de entorno de pruebas (staging); cambios llegan directamente a producción sin validación intermedia.<br><br>Mitigación: crear branch de staging y un entorno de despliegue separado.                                   | Alta         | Alto    |
| Planificación  | Desviación en la estimación de historias complejas (correlatividades, detección de riesgo de abandono) sin puntos de referencia previos.<br><br>Mitigación: usar Planning Poker y revisar velocidad del equipo sprint a sprint. | Alta         | Crítico |
| Planificación  | Sección 'Cambios de alcance' vacía: indica falta de proceso formal para gestionar cambios de requerimientos.<br><br>Mitigación: documentar todo cambio de alcance y re-estimar impacto en el sprint.                            | Media        | Medio   |
| Humano         | Conocimiento concentrado: si el experto de un módulo no está disponible, el equipo no puede avanzar ni dar soporte.<br><br>Mitigación: pair programming y documentación técnica mínima por módulo.                              | Media        | Alto    |


Estas son los riegos que no encontró la IA y el equipo si

| Tipo           | Descripción y mitigación sugerida                                                                                                  | Probabilidad | Impacto    |
| -------------- | ---------------------------------------------------------------------------------------------------------------------------------- | ------------ | ---------- |
| Humano         | Podría a ver conflictos entre los liderazgos al cambiar los scrum master dado que cada persona puede dirigir de diferentes maneras | Baja         | Medio      |
| Organizacional | Conflictos entre integrantes del equipo a trabajar los mismos módulos a la vez                                                     | Alta         | Bajo-medio |
La conclusión sobre la calidad del análisis es que la IA ofrece una visión mucho más profunda, ya que identifica riesgos a nivel técnico, de planificación y organizacional que no hubiéramos detectado debido a nuestra falta de experiencia.  Por otro lado, los riesgos que nosotros identificamos están más basados en nuestra experiencia personal.

Por ello, lo es combinar ambos enfoques: integrar los riesgos detectados por la IA con los nuestros, lo que permite obtener una visión más completa del proyecto.