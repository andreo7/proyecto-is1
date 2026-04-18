```mermaid
flowchart TD
    CW(["Cliente Web - Browser"])

    subgraph Backend ["Backend — Java 11"]
        SJ["Spark Java - Servidor HTTP + Rutas"]
        MV["Motor de Vistas - Mustache"]
        ORM["ActiveJDBC - ORM"]
        BC["BCrypt - Hash y verificación de contraseñas"]
    end

    DB[("Base de Datos - SQLite")]

    CW -- "HTTP Request GET/POST" --> SJ
    SJ -- "Registro: hashea contraseña" --> BC
    SJ -- "Login: verifica contraseña" --> BC
    BC -- "Hash almacenable / Resultado true o false" --> SJ
    SJ -- "Datos del modelo" --> MV
    MV -- "HTML Response" --> CW
    SJ <-- "Consultas / Resultados" --> ORM
    ORM <-- "SQL" --> DB
```
