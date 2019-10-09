# Configuration report

!!! note
    During startup guicey records startup metrics and remembers all details of configuration process. 
    All this information is available through GuiceyConfigurationInfo bean:
    
    ```java
    @Inject GuiceyConfigurationInfo info;
    ``` 

Configuration diagnostic report is the most commonly used report allowing you to see guicey startup and 
configuration details (the last is especially important for de-duplication logic diagnostic).

```java
GuiceBundle.builder() 
    ...
    .printDiagnosticInfo()
    .build());
```   

Report intended to answer:

* [How guicey spent time](#timings)
* [What options used](#used-options)
* [What was configured](#configuration-summary)
* [From where configuration items come from](#configuration-tree)


Example report:

```    
INFO  [2019-10-09 04:34:33,624] ru.vyarus.dropwizard.guice.debug.ConfigurationDiagnostic: Diagnostic report

---------------------------------------------------------------------------[STARTUP STATS]

    GUICEY started in 377.9 ms (68.23 ms config / 307.8 ms run / 1.901 ms jersey)
    │   
    ├── [0.80%] CLASSPATH scanned in 3.621 ms
    │   ├── scanned 5 classes
    │   └── recognized 4 classes (80% of scanned)
    │   
    ├── [5.6%] BUNDLES processed in 21.32 ms
    │   └── 6 initialized in 20.17 ms
    │   
    ├── [2.9%] COMMANDS processed in 11.86 ms
    │   └── registered 2 commands
    │   
    ├── [12%] MODULES processed in 45.50 ms
    │   ├── 5 modules autowired
    │   ├── 7 elements found in 4 user modules in 41.13 ms
    │   └── 0 extensions detected from 2 acceptable bindings
    │   
    ├── [8.8%] INSTALLERS processed in 33.21 ms
    │   ├── registered 12 installers
    │   └── 3 extensions recognized from 9 classes in 10.49 ms
    │   
    ├── [58%] INJECTOR created in 218.9 ms
    │   ├── Module execution: 136 ms
    │   ├── Interceptors creation: 1 ms
    │   ├── TypeListeners & ProvisionListener creation: 2 ms
    │   ├── Converters creation: 1 ms
    │   ├── Binding creation: 21 ms
    │   ├── Binding initialization: 31 ms
    │   ├── Collecting injection requests: 2 ms
    │   ├── Static validation: 3 ms
    │   ├── Instance member validation: 4 ms
    │   ├── Static member injection: 8 ms
    │   ├── Instance injection: 2 ms
    │   └── Preloading singletons: 5 ms
    │   
    ├── [1.3%] EXTENSIONS installed in 5.424 ms
    │   ├── 3 extensions installed
    │   └── declared as: 2 manual, 1 scan, 0 binding
    │   
    ├── [0.27%] JERSEY bridged in 1.901 ms
    │   ├── using 2 jersey installers
    │   └── 2 jersey extensions installed in 739.5 μs
    │   
    └── [11%] remaining 40 ms


---------------------------------------------------------------------------[OPTIONS]

    Guicey                    (r.v.dropwizard.guice.GuiceyOptions)
        ScanPackages                   = [ru.vyarus.dropwizard.guice.diagnostic.support.features] *CUSTOM
        SearchCommands                 = true                           *CUSTOM
        UseCoreInstallers              = true                           
        BindConfigurationByPath        = true                           
        AnalyzeGuiceModules            = true                           
        InjectorStage                  = PRODUCTION                     
        GuiceFilterRegistration        = [REQUEST]                      
        UseHkBridge                    = false                          


    Installers                (r.v.d.g.m.i.InstallersOptions)
        JerseyExtensionsManagedByGuice = true                           
        ForceSingletonForJerseyExtensions = true                           


---------------------------------------------------------------------------[CONFIGURATION]

    COMMANDS = 
        Cli                          (r.v.d.g.d.s.features)     *SCAN
        EnvCommand                   (r.v.d.g.d.s.features)     *SCAN, GUICE_ENABLED


    BUNDLES = 
        Foo2Bundle                   (r.v.d.g.d.s.bundle)       
            FooBundleRelative2Bundle     (r.v.d.g.d.s.bundle)       
        HK2DebugBundle               (r.v.d.g.m.j.debug)        *HOOK, REG(1/2)
        GuiceRestrictedConfigBundle  (r.v.d.g.support.util)     *HOOK
        CoreInstallersBundle         (r.v.d.g.m.installer)      
            WebInstallersBundle          (r.v.d.g.m.installer)      


    INSTALLERS and EXTENSIONS in processing order = 
        jerseyfeature        (r.v.d.g.m.i.f.j.JerseyFeatureInstaller) *REG(1/2)
            HK2DebugFeature              (r.v.d.g.m.j.d.service)    
        resource             (r.v.d.g.m.i.f.j.ResourceInstaller)    
            FooBundleResource            (r.v.d.g.d.s.bundle)       *REG(1/3)
            FooResource                  (r.v.d.g.d.s.features)     *SCAN


    GUICE MODULES = 
        FooModule                    (r.v.d.g.d.s.features)     *REG(2/2)
        FooBundleModule              (r.v.d.g.d.s.bundle)       
        HK2DebugModule               (r.v.d.g.m.j.d.HK2DebugBundle) 
        GRestrictModule              (r.v.d.g.s.u.GuiceRestrictedConfigBundle) 
        GuiceBootstrapModule         (r.v.d.guice.module)       


---------------------------------------------------------------------------[CONFIGURATION TREE]

    APPLICATION
    ├── extension  FooBundleResource            (r.v.d.g.d.s.bundle)       
    ├── extension  -FooBundleResource           (r.v.d.g.d.s.bundle)       *DUPLICATE
    ├── module     FooModule                    (r.v.d.g.d.s.features)     
    ├── module     GuiceBootstrapModule         (r.v.d.guice.module)       
    ├── -disable   LifeCycleInstaller           (r.v.d.g.m.i.feature)      
    │   
    ├── Foo2Bundle                   (r.v.d.g.d.s.bundle)       
    │   ├── extension  -FooBundleResource           (r.v.d.g.d.s.bundle)       *DUPLICATE
    │   ├── module     FooBundleModule              (r.v.d.g.d.s.bundle)       
    │   ├── -disable   ManagedInstaller             (r.v.d.g.m.i.feature)      
    │   │   
    │   └── FooBundleRelative2Bundle     (r.v.d.g.d.s.bundle)       
    │       └── module     FooModule#2                  (r.v.d.g.d.s.features)     
    │   
    ├── HK2DebugBundle               (r.v.d.g.m.j.debug)        
    │   ├── installer  JerseyFeatureInstaller       (r.v.d.g.m.i.f.jersey)     
    │   ├── extension  HK2DebugFeature              (r.v.d.g.m.j.d.service)    
    │   └── module     HK2DebugModule               (r.v.d.g.m.j.d.HK2DebugBundle) 
    │   
    ├── CoreInstallersBundle         (r.v.d.g.m.installer)      
    │   ├── installer  -JerseyFeatureInstaller      (r.v.d.g.m.i.f.jersey)     *DUPLICATE
    │   ├── installer  ResourceInstaller            (r.v.d.g.m.i.f.jersey)     
    │   └── WebInstallersBundle          (r.v.d.g.m.installer)      
    │   
    ├── CLASSPATH SCAN
    │   └── extension  FooResource                  (r.v.d.g.d.s.features)     
    │   
    └── HOOKS
        ├── -HK2DebugBundle              (r.v.d.g.m.j.debug)        *DUPLICATE
        │   
        └── GuiceRestrictedConfigBundle  (r.v.d.g.support.util)     
            └── module     GRestrictModule              (r.v.d.g.s.u.GuiceRestrictedConfigBundle) 
```

## Timings

### Startup timings

```
    GUICEY started in 453.3 ms
```

Overall guicey time measured: GuiceBundle methods plus part of HK2 configuration time (HK2 started after bundle).
All items below represent guicey time detalization. Items always detail time of direct parent.

Most of this time actually spent on class loading. For example, report above represent [test](https://github.com/xvik/dropwizard-guicey/blob/master/src/test/groovy/ru/vyarus/dropwizard/guice/config/debug/DiagnosticBundleTest.groovy) direct execution. 
But when this test executed as part of suit time become  `GUICEY started in 52.95 ms` because most classes were pre-loaded by other tests.

### Classpath scan

```
    ├── [0,88%] CLASSPATH scanned in 4.282 ms
    │   ├── scanned 5 classes
    │   └── recognized 4 classes (80% of scanned)
```

Classpath scan performed just once. Represents only time spent resolving all classes in configured packages. Guicey will later use this resolved classes to search commands (if enabled), installers and extensions. 

`scanned 5 classes` means that 5 classed were found (overall) in configured packages. `recognized 4 classes` Show effectiveness of classpath scanning (how many classes were actually used as installer, extension or command).

NOTE: classpath scan time will be obviously bigger for larger classes count. But most of this time are actually class loading time. If you use all these classes then they will be loaded in any case. If you disable classpath scan to save time then this time move to other some place (for example, to injector creation). 

### Commands

```
    ├── [4,2%] COMMANDS processed in 19.10 ms
    │   └── registered 2 commands
```

Commands time includes time spent on commands search (in classes from classpath scan; if enabled .searchCommands()) and calling .injectMemebers on configured environment commands (last part is always performed, but it's very fast so most likely commands section will not appear if .searchCommands() is not enabled)

### Bundles

```
    ├── [6,4%] BUNDLES processed in 29.72 ms
    │   ├── 2 resolved in 8.149 ms
    │   └── 6 processed
```

Bundles time includes bundles lookup time (if not .disableBundleLookup()), dropwizard bunles lookup (if .configureFromDropwizardBundles()) and bundles execution.

`2 resolved in 8.149 ms` indicated bundles resolved with guicey bundle lookup or from dropwizard bundles.
`6 processed` - overall processed bundles (all registered bundles, including just resolved).

### Injector

```
    ├── [86%] INJECTOR created in 390.3 ms
    │   ├── installers prepared in 13.79 ms
    │   │   
    │   ├── extensions recognized in 9.259 ms
    │   │   ├── using 11 installers
    │   │   └── from 7 classes
    │   │   
    │   └── 3 extensions installed in 4.188 ms
```

All installers and extensions operations (except jersey related features) performed inside of guice module and so included into overall injector creation time.

`installers prepared in 13.79 ms` include installers search in classpath (if scan enabled), instantiation and preparing for usage (remove duplicates, sort).

`extensions recognized in 9.259 ms` - all manually configured extensions and all classes from classpath scan (`from 7 classes`) are recognized by all registered installers (`using 11 installers`) using installer match method. Recognized installers bound to guice context (or custom action performed for binding installers).

`3 extensions installed in 4.188 ms` - all recognized extensions are installed with installers install methods.

!!! note
    Most time of injector creation is internal guice logic. You can enable guice logs to see more details (see below)

### HK2

```
    ├── [1,3%] HK2 bridged in 6.583 ms
    │   ├── using 2 jersey installers
    │   └── 2 jersey extensions installed in 660.9 μs
```

Jersey starts after dropwizard bundles processing and so after GuiceBundle execution. This time is tracked as (overall) guicey time. Here guicey register required HK2 bindings and (some HK2 beans in guice) and executes jersey installers (installers implement JerseyInstaller) to process jersey specific features. For example, all resources and jersey extensions installed here (because requires HK2 specific bindings).

Note that installation time (`2 jersey extensions installed in 660.9 μs`) is so tiny just because empty resources (without methods) were used. In real application installation time will be bigger.

### Remaining

```
    └── [1,1%] remaining 5 ms
```

Represent not explicitly tracked time, spent by guicey for other small operations. Shown on tree to indicate that all major parts were shown.

## Used options

Shows all set or requested (by application logic) options. If you use your own options here they will also be printed.

``` 
    Guicey                    (r.v.dropwizard.guice.GuiceyOptions)
        ScanPackages                   = [ru.vyarus.dropwizard.guice.diagnostic.support.features] *CUSTOM
        SearchCommands                 = true                           *CUSTOM
        UseCoreInstallers              = true                           
        ConfigureFromDropwizardBundles = false                          
        InjectorStage                  = PRODUCTION                     
        GuiceFilterRegistration        = [REQUEST]     
        
```

Used markers:
* CUSTOM - option value set by user
* NOT_USED - option was set by user but never used

Not used marker just indicated that option is "not yet" used. Options may be consumed lazilly by application logic, so
it is possible that its not used at reporting time. There is no such cases with guicey options, but may be with your custom options (it all depends on usage scenario).

## Configuration summary

Section intended to compactly show all configuration (to quickly see what was configured).

This and the next sections used condensed package notion:

```
CoreInstallersBundle         (r.v.d.g.m.installer)    
```

Assumed that all classes in application will be uniquely identifiable by name and package info shown just to be able to 
understand exact class location. Logback shrinker used.

Report also indicates duplicate registrations by REG(N) marker, where N - amount of installations 
(ignored installations will be visible in configuration tree). Counted:

* item registration in different places (e.g. different bundles)
* duplicate registrations in simgle place (e.g. `.extensions(MyExt.class, MyExt.class)`)

### General

All configuration items (commands, modules, installers, extension, bundles) are identified by class. Duplicate entities are not allowed and simply ignored.

For example, if extension registered manually and by classpath scan then it will be registered once, but internally guicey will remember both configuration sources.

In contrast to other items, bundles and modules are registered by instance, but still uniqueness is checked by type: only first instance registered and other instances considered as duplicate.

### Commands

```
    COMMANDS = 
        Cli                          (r.v.d.g.d.s.features)     *SCAN
        EnvCommand                   (r.v.d.g.d.s.features)     *SCAN,GUICE_ENABLED
```

Shows commands resolved with classpath scan (enabled with .searchCommands()).

The following markers used:

* SCAN - item from classpath scan (always)
* GUICE_ENABLED - marks environment command, which could contain guice injections (other commands simply doesn't trigger application run and so injector never starts)

### Bundles

```
    BUNDLES = 
        CoreInstallersBundle         (r.v.d.g.m.installer)      
        Foo2Bundle                   (r.v.d.g.d.s.bundle)       
            FooBundleRelative2Bundle     (r.v.d.g.d.s.bundle)       
        HK2DebugBundle               (r.v.d.g.m.j.debug)        *LOOKUP, REG(2)
        DiagnosticBundle             (r.v.d.g.m.c.debug)        
        GuiceRestrictedConfigBundle  (r.v.d.g.support.util)     *LOOKUP
```

All registered bundles are shown as a tree (to indicate transitive bundles).

The following markers used:

* LOOKUP - bundle resolved with bundle lookup mechanism
* DW - bundle recognized from registered dropwizard bundle

### Installers and extensions

```
    INSTALLERS and EXTENSIONS in processing order = 
        jerseyfeature        (r.v.d.g.m.i.f.j.JerseyFeatureInstaller) *REG(2)
            HK2DebugFeature              (r.v.d.g.m.j.d.service)    
        resource             (r.v.d.g.m.i.f.j.ResourceInstaller)    
            FooBundleResource            (r.v.d.g.d.s.bundle)       *REG(3)
            FooResource                  (r.v.d.g.d.s.features)     *SCAN
```

Shows used installers (only installers which install extensions) and installed extensions. 

Both installers and extensions are shown in the processing order (sorted according to @Order annotations).

The following markers used:

* SCAN - item from classpath scan (even if extension or installer were registered manually also to indicate item presence in classpath scan)
* LAZY - extensions annotated with `@LazyBinding`
* HK2 - extension annotated with `@HK2Managed`
* HOOK - registered by [configuration hook](../configuration.md#guicey-configuration-hooks)

### Modules

```
    GUICE MODULES = 
        FooModule                    (r.v.d.g.d.s.features)     *REG(2)
        FooBundleModule              (r.v.d.g.d.s.bundle)       
        HK2DebugModule               (r.v.d.g.m.j.d.HK2DebugBundle) 
        DiagnosticModule             (r.v.d.g.m.c.d.DiagnosticBundle) 
        GRestrictModule              (r.v.d.g.s.u.GuiceRestrictedConfigBundle) 
        GuiceBootstrapModule         (r.v.d.guice.module)  
```

All registered guice modules.

## Configuration tree

Configuration tree is useful to understand from where configuration items come from.

Installer disables are shown like:

```
    ├── -disable   LifeCycleInstaller           (r.v.d.g.m.i.feature) 
```

Duplicate registrations (ignored by guicey) are shown like:

```
    │   ├── extension  FooBundleResource            (r.v.d.g.d.s.bundle)       *IGNORED
```

If number in configuration report (e.g.REG(3)) doesn't match registration appearances, then item registered
multiple times in one of this places.

Note that CoreInstallersBundle are always below all other bundles. This is because it always registered last 
(to be able to disable it's registration). It doesn't affect anything other than reporting (because bundles order does 
not change anything except this tree).

## Re-using

Diagnostic info rendering may be used for custom rendering (showing in web page or some other staff).
Rendering is performed with 3 beans, available for injection (when bundle registered):

* [StatsRenderer](https://github.com/xvik/dropwizard-guicey/blob/master/src/main/java/ru/vyarus/dropwizard/guice/module/context/debug/report/stat/StatsRenderer.java)
* [OptionsRenderer](https://github.com/xvik/dropwizard-guicey/blob/master/src/main/java/ru/vyarus/dropwizard/guice/module/context/debug/report/option/OptionsRenderer.java)
* [DiagnosticRenderer](https://github.com/xvik/dropwizard-guicey/blob/master/src/main/java/ru/vyarus/dropwizard/guice/module/context/debug/report/diagnostic/DiagnosticRenderer.java)
* [ContextTreeRenderer](https://github.com/xvik/dropwizard-guicey/blob/master/src/main/java/ru/vyarus/dropwizard/guice/module/context/debug/report/tree/ContextTreeRenderer.java) 

!!! note
    Renderers are also available in [event objects](../events.md#event-structure) (for events fired after injection creation) 


## Guice injector creation timings

You will see in guicey timings that almost all time spent creating guice injector. 
To see some guice internal timings enable guice debug logs:

```
logging:
  loggers:
    com.google.inject.internal.util: DEBUG
```

Logs will be something like this:

```
DEBUG [2016-08-03 21:09:45,963] com.google.inject.internal.util.Stopwatch: Module execution: 272ms
DEBUG [2016-08-03 21:09:45,963] com.google.inject.internal.util.Stopwatch: Interceptors creation: 1ms
DEBUG [2016-08-03 21:09:45,965] com.google.inject.internal.util.Stopwatch: TypeListeners & ProvisionListener creation: 2ms
DEBUG [2016-08-03 21:09:45,966] com.google.inject.internal.util.Stopwatch: Scopes creation: 1ms
DEBUG [2016-08-03 21:09:45,966] com.google.inject.internal.util.Stopwatch: Converters creation: 0ms
DEBUG [2016-08-03 21:09:45,992] com.google.inject.internal.util.Stopwatch: Binding creation: 26ms
DEBUG [2016-08-03 21:09:45,992] com.google.inject.internal.util.Stopwatch: Module annotated method scanners creation: 0ms
DEBUG [2016-08-03 21:09:45,993] com.google.inject.internal.util.Stopwatch: Private environment creation: 1ms
DEBUG [2016-08-03 21:09:45,993] com.google.inject.internal.util.Stopwatch: Injector construction: 0ms
DEBUG [2016-08-03 21:09:46,170] com.google.inject.internal.util.Stopwatch: Binding initialization: 177ms
DEBUG [2016-08-03 21:09:46,171] com.google.inject.internal.util.Stopwatch: Binding indexing: 1ms
DEBUG [2016-08-03 21:09:46,172] com.google.inject.internal.util.Stopwatch: Collecting injection requests: 1ms
DEBUG [2016-08-03 21:09:46,179] com.google.inject.internal.util.Stopwatch: Binding validation: 7ms
DEBUG [2016-08-03 21:09:46,183] com.google.inject.internal.util.Stopwatch: Static validation: 4ms
DEBUG [2016-08-03 21:09:46,191] com.google.inject.internal.util.Stopwatch: Instance member validation: 8ms
DEBUG [2016-08-03 21:09:46,192] com.google.inject.internal.util.Stopwatch: Provider verification: 1ms
DEBUG [2016-08-03 21:09:46,201] com.google.inject.internal.util.Stopwatch: Static member injection: 9ms
DEBUG [2016-08-03 21:09:46,204] com.google.inject.internal.util.Stopwatch: Instance injection: 3ms
DEBUG [2016-08-03 21:09:46,427] com.google.inject.internal.util.Stopwatch: Preloading singletons: 223ms
```

!!! note 
    'Preloading singletons' line will be logged **long after** other guice log messages, so search it at the end of your startup log.