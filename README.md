# Skip Versioning Demo

Demo project presenting how to skip versioning on normal workflow documents
by adding a mixin type and customizing ```VersionVariantAction``` SCXML action class.

## Use Cases

- When you import content from the external data source (which should be regarded as the "single source of the truth")
  to Hippo documents periodically, you normally don't want to create revision history for that kind of imported documents.
  By default, whenever you import and republish the imported documents, CMS will keep making versions, which can cause
  the database of CMS can easily bloat.

## Build and Run the Demo

```bash
    $ mvn clean package && mvn -Pcargo.run
```

- Visit http://localhost:8080/cms/.
- Edit News or Events document and try to publish it. You will see it creating a new version.
- Edit Product document instead and try to publish it. It doesn't create a version
  because the unpublished variant node of Product document is type of ```skipversiondemo:skipversioning``` mixin type and [SkippableVersionVariantAction.java](cms/src/main/java/org/example/skip/version/demo/cms/workflow/action/SkippableVersionVariantAction.java) which must be configured at
  ```/hippo:configuration/hippo:modules/scxmlregistry/hippo:moduleconfig/hipposcxml:definitions/documentworkflow/version/@hipposcxml:classname```

- For your information, as the ```SkippableVersionVariantAction``` is located in ```cms``` subproject and it depends on Hippo Workflow API, the [cms/pom.xml](cms/pom.xml) contains the following dependency for compilation:
```xml
    <!-- For Custom SCXML Action implementation  -->
    <dependency>
      <groupId>org.onehippo.cms7</groupId>
      <artifactId>hippo-repository-workflow</artifactId>
    </dependency>
```

- You can also add a new Product document. Try to edit and publish it, but it still doesn't create a version at all as expected.

## Summary

If you want to apply this solution in your own project, you should do the following:

- Define a custom mixin type, on which mixin type you can skip versioning,  in your project's CND file like the following example:
```
[skipversiondemo:skipversioning]
  mixin
```
- Add a custom version action class like the example, [SkippableVersionVariantAction.java](cms/src/main/java/org/example/skip/version/demo/cms/workflow/action/SkippableVersionVariantAction.java) in your ```cms``` project.
- Configure or bootstrap to replace the default custom version action class name with your classname in the repository. The configuration property location is ```/hippo:configuration/hippo:modules/scxmlregistry/hippo:moduleconfig/hipposcxml:definitions/documentworkflow/version/@hipposcxml:classname```
- Add the custom mixin type to the specific document type's prototype node, so that it won't create versions for newly created documents of that specific document type.
- Also, add the custom mixin type to the 'unpublished' (or 'preview) variant nodes of any existing documents, so that it won't create versions when you update and publish the documents.
