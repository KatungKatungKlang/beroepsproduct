# Beroepsproduct Data Structures & Algorithms

## Groepsleden:
- Sameer Kewal: SE/1121/024
- Gavin Soemotinojo: SE/1121/053
- Amisha Boedhoe: SE/1121/012


## Design Patterns:
- Facade: 
  - Ons Facade is de ApiCalls method, omdat deze de HTTP requests naar de API handelt.
  Verder is het verantwoordelijk voor het omzetten van de responses naar JSON en het halen van de informatie die we nodig hebben uit de responses.
  Men kan dus de ApiCalls method gebruiken, zonder te weten hoe HTTP requests te doen.
  Verder als er veranderingen zijn binnen de APIClass, zal de client code er niets van merken
- Singleton:
  - De Game class is de singleton. Dit hebben wij gedaan, zodat als men ergens anders de instance van de Game class
  opnieuw oproept, men hetzelfde spel dat op dat moment gaande is, zal krijgen.

## Data Structures:
- Hashmap:
  - Deze hebben wij gebruikt, om het aantal  occurences van kaarten
die voorkomen in een pile te tellen. Elke nummerkaart vormt een nieuwe key en the occurences zijn de values.
- Array:
  - Wij hebben ook veel gebruik gemaakt van Arrays, bijvoorbeeld in het begin van het spel, wanneer wij weten
  dat wij maar 7 kaarten nodig zullen hebben. In dit geval, komt de grootte van de array goed uit, omdat wij nooit meer dan 7 kaarten nodig zullen hebben. 