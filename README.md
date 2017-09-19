# Skip Versioning Demo

Demo project presenting how to skip versioning on normal workflow documents
by adding a mixin type and customizing ```VersionVariantAction``` SCXML action class.

## Build and Run the Demo

```bash
    $ mvn clean package && mvn -Pcargo.run
```

- Visit http://localhost:8080/cms/.
- Edit News or Events document and try to publish it. You will see it creating a new version.
- Edit Product document instead and try to publish it. It doesn't create a version
  because the unpublished variant node of Product document is type of ```skipversiondemo:skipversioning``` mixin type and [SkippableVersionVariantAction.java](cms/src/main/java/org/example/skip/version/demo/cms/workflow/action/SkippableVersionVariantAction.java) which is configured at
  ```/hippo:configuration/hippo:modules/scxmlregistry/hippo:moduleconfig/hipposcxml:definitions/documentworkflow/version/@hipposcxml:classname```.
- You can also add a new Product document. Try to edit and publish it, but it still doesn't create a version at all.

