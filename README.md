# ANT IVY MONOREPO EXAMPLE

För att testa det här repot behöver man ha Ant och Ivy installerat. Instruktioner för installation av Ant finns [här](https://ant.apache.org/manual/install.html).

När man har Ant installerat kan man köra följande kommando för att installera Ivy:
```ant install-ivy``` <--- TODO: Lägg till target

## Projekt

I repot hittar vi tre project *TheService*, *TheClient* och *TheLibrary*. *TheService* producerar tre artifakter och *TheClient* och *TheLibrary* producerar en artifakt varderara. Bilden nedan visar hur  beroendena ser ut.

![artifacts-and-dependencies](./docs/artifacts-and-dependencies.png) 

## Monorepo

Projekten ligger (som ni ser) samlade under ett repository, ett så kallat Monorepo. Med hjälp av Ant och en funktion som kallas *subant* kan vi med ett kommando i roten trigga ant targets i flera av de underliggande projekten. Ivy används för att hantera både externa och interna beroenden, vi kommer in på det mer under en rubrik längre ner.

![monorepo-subant](./docs/monorepo-subant.png)

För att bygga alla projekt kör följande kommando från roten:
```ant build```

För att rensa ett bygga kan man köra:
```ant clean```

## Ivy

Ivy är en extension till Ant som hanterar beroenden. När man installerat Ivy behöver man också lägga med följande i "project"-taggen i *build.xml*:

```xml
<project name="theprojectname" xmlns:ivy="antlib:org.apache.ivy.ant">
...
</project>
```

Detta ger ett antal nya targets som man kan använda för att trigga funktionalitet från Ivy. Nedanstående bild visar hur olika Ivy targets flyttar beroenden:

![ivy-tasks](./docs/ivy-tasks.png) 

*Public Repository* motsvarar då Maven Central (eller motsvarande). *Enterprise Repository* motsvarar ett eget driftat artifactory (exempelvis JFrog). *Cache* ligger lokalt på din dator (normalt under .ivy2 i användarkatalogen). Det som inte syns i bilden är att man även kan göra en *Publish* till den lokala *Cachen* vilket är vad som görs för att hantera de interna beroendena under ett bygge. 

### ivy.xml

Varje projekt som använder Ivy ska ha en *ivy.xml* i roten av projektet. I *ivy.xml* ligger information om projektet och dess artifakter samt en lista över vilka beroenden projektet har. 

...TO BE CONTINUED...