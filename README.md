# json-schema-inliner

Inliner for JSON schemas

To use the SBT plugin:

- in plugins.sbt
```
resolvers += Resolver.bintrayRepo("commercetools", "sbt-plugins")

addSbtPlugin("io.sphere" % "json-schema-inliner" % "x.y.z")
```
- build.sbt
```
enablePlugins(JsonSchemaInlinerPlugin)
```

The json schemas found in `src/main/resources` will be inlined into `target/scala_2.xx/resource_managed/main/inline`.
