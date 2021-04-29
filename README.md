# ANT IVY MONOREPO EXEMPEL

För att testa det här repot behöver man ha Ant och Ivy installerat. Instruktioner för installation av Ant finns [här](https://ant.apache.org/manual/install.html).

När man har Ant installerat kan man köra följande kommando för att installera Ivy:
```ant install-ivy```

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

Som exempel ser ivy.xml för *TheClient* ut såhär:

```xml
<ivy-module version="2.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="http://ant.apache.org/ivy/schemas/ivy.xsd">

    <info organisation="se.woop" module="theclient" />

    <publications>
        <artifact name="theclient" type="jar" ext="jar" />
    </publications>

    <dependencies>
        <dependency org="commons-lang" name="commons-lang" rev="2.6" conf="*->default" />
        <dependency org="se.woop" name="theservice" rev="latest.integration" conf="*->client" />
    </dependencies>

</ivy-module>
```

Första taggen *info* innehåller info om projektet (modulen). Andra taggen *publications* innehåller information om de artifakter som publiceras från det här projektet. I exemplet ovan är det bara en, men det kan potentiellt vara flera. Tredje taggen *dependencies* innehåller en lista över de beroenden som projektet har. Både till interna projektberoenden som *common-lang* ovan och till interna projektberoenden som *theservice* ovan. 

Variabeln *rev* specificerar vilken version av beroendet man är intressera av. Till interna beroenden kan man använda *latest.integration* för att hela tiden få den senast byggda versionen av givet beroende.

Variabeln *conf* används för att specificera vilka transitiva beroenden man vill inkludera från beroendet. Om vi tar `*->client` som exempel så betyder det ungefär: *för alla typer av byggen är den här modulen beroende av alla transitiva beroenden av typen 'client'*. Det före pilen (`->`) specificerar för vilken eller vilka typer de specificerade typerna efter pilen gäller. Hade vi för beroendet till *theservice* ovan inte specificerat bara *client* hade vi även fått med två artifakter till för *service* och *schema*, eftersom *theservice* publicerar 3 olika artifakter.

Behöver man styra beroenden på en finare nivå än vad som är möjligt med *conf* finns två typer av barnelement man kan använda innanför *dependency* elementet, *include* och *exclude*. Dessa kan användas för att inkludera eller exkludera specifika transitiva beroenden. 

### ivysettings.xml

Utan att ha specificerat en egen *ivysettings.xml* kommer ivy använda sig av ett gäng standardinställningar. 

