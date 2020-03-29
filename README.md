# Model C4 generator based on Structurizr with PlantUML extension

There is a skeleton project showing how to model C4 in Java with PlantUML 
excluding website. It follows OOP approach in place of  structural files.

Run following command to generate charts:
```sh
./gradlew run
```

Look at:
* c1.puml

```plantuml
@startuml(id=SystemContext)
scale max 2000x2000
title Platforma bankowości online - System Context
caption Diagram kontekstowy systemu bankowego

skinparam {
  shadowing false
  arrowColor #707070
  actorBorderColor #707070
  componentBorderColor #707070
  rectangleBorderColor #707070
  noteBackgroundColor #ffffff
  noteBorderColor #707070
  rectangleFontColor #ffffff
  rectangleStereotypeFontColor #ffffff
}
actor "Klient indywidualny" <<Person>> as 1 #08427b
note right of 1
  Klient banku, posiada konta
  bankowe z numerami
end note
package "Bankowość korporacyjna" {
  rectangle 4 <<Software System>> #1168bd [
    E-mail System
    --
    System e-mailowy Microsoft
    Exchange
  ]
  rectangle 2 <<Software System>> #1168bd [
    Platforma bankowości online
    --
    Pozwala na przeglądanie stanu
    konta i dokonywanie
    transakcji
  ]
  rectangle 3 <<Software System>> #1168bd [
    System bankowy Mainframe
    --
    Core'owy system bankowy.
    Zawiera informacje o
    klientach, transakcjach,
    kontach, etc.
  ]
}
4 .[#707070].> 1 : Wysyła e-maile do
1 .[#707070].> 2 : Używa
2 .[#707070].> 4 : Pobiera informacje/Wysyła informacje
2 .[#707070].> 3 : <<XML/HTTPS>>\nPobiera informacje/Wysyła informacje
@enduml
```

* c2.puml

```plantuml
@startuml(id=Containers)
scale max 2000x2000
title Platforma bankowości online - Containers
caption Diagram kontenerów systemu Platformy Bankowości Online

skinparam {
  shadowing false
  arrowColor #707070
  actorBorderColor #707070
  componentBorderColor #707070
  rectangleBorderColor #707070
  noteBackgroundColor #ffffff
  noteBorderColor #707070
  rectangleFontColor #ffffff
  rectangleStereotypeFontColor #ffffff
}
rectangle 4 <<Software System>> #1168bd [
  E-mail System
  --
  System e-mailowy Microsoft
  Exchange
]
actor "Klient indywidualny" <<Person>> as 1 #08427b
note right of 1
  Klient banku, posiada konta
  bankowe z numerami
end note
rectangle 3 <<Software System>> #1168bd [
  System bankowy Mainframe
  --
  Core'owy system bankowy.
  Zawiera informacje o
  klientach, transakcjach,
  kontach, etc.
]
package "Platforma bankowości online" <<Software System>> {
  rectangle 10 <<Container>> #bbbbbb [
    API
    --
    Dostarcza funkcjonalność
    bankowości online poprzez
    JSON/HTTPS API.
  ]
  rectangle 9 <<Container>> #bbbbbb [
    Aplikacja mobilna
    --
    Dostarcza ograniczoną
    funkcjonalność bankowości
    online dla klientów
  ]
  database 11 <<Container>> #1168bd [
    Baza danych
    --
    Informacje o klientach,
    hashowane hasła, logi etc
  ]
}
10 .[#707070].> 11 : <<JDBC>>\nCzyta/Zapisuje
10 .[#707070].> 4 : <<SMTP>>\nWysyła maile
10 .[#707070].> 3 : <<XML/HTTPS>>\nUżywa
9 .[#707070].> 10 : <<JSON/HTTPS>>\nUżywa
4 .[#707070].> 1 : Wysyła e-maile do
1 .[#707070].> 9 : Używa
@enduml
```

* c3.puml

```plantuml
@startuml(id=Components)
scale max 2000x1409
title Platforma bankowości online - API - Components
caption Diagram komponentów aplikacji API

skinparam {
  shadowing false
  arrowColor #707070
  actorBorderColor #707070
  componentBorderColor #707070
  rectangleBorderColor #707070
  noteBackgroundColor #ffffff
  noteBorderColor #707070
  rectangleFontColor #ffffff
  rectangleStereotypeFontColor #ffffff
}
rectangle 9 <<Container>> #bbbbbb [
  Aplikacja mobilna
  --
  Dostarcza ograniczoną
  funkcjonalność bankowości
  online dla klientów
]
database 11 <<Container>> #1168bd [
  Baza danych
  --
  Informacje o klientach,
  hashowane hasła, logi etc
]
rectangle 4 <<Software System>> #1168bd [
  E-mail System
  --
  System e-mailowy Microsoft
  Exchange
]
rectangle 3 <<Software System>> #1168bd [
  System bankowy Mainframe
  --
  Core'owy system bankowy.
  Zawiera informacje o
  klientach, transakcjach,
  kontach, etc.
]
package "API" <<Container>> {
  component 22 <<Komponent: Spring Bean>> #dddddd [
    Fasada do Mainframe Banking System
    --
    Fasada do mainframe banking
    system
  ]
  component 21 <<Komponent: Spring Bean>> #dddddd [
    Komponent E-mail
    --
    Pozwala na wysyłanie e-maili
  ]
  component 20 <<Komponent: Spring Bean>> #dddddd [
    Komponent bezpieczeństwa
    --
    Dostarcza funkcjonalność
    potrzebną do zmian hasła
  ]
  component 18 <<Komponent: Spring MVC Rest Controller>> #dddddd [
    Konta bankowe
    --
    Pozwala na wgląd w widok
    posiadanych kont
  ]
  component 17 <<Komponent: Spring MVC Rest Controller>> #dddddd [
    Logowanie
    --
    Pozwala na logowanie do
    platformy bankowej
  ]
  component 19 <<Komponent: Spring MVC Rest Controller>> #dddddd [
    Resetowanie hasła
    --
    Pozwala na wygenerowanie URL
    do resetu hasła
  ]
}
9 .[#707070].> 18 : <<JSON/HTTPS>>\nUżywa
9 .[#707070].> 17 : <<JSON/HTTPS>>\nUżywa
9 .[#707070].> 19 : <<JSON/HTTPS>>\nUżywa
22 .[#707070].> 3 : <<XML/HTTPS>>\nUżywa
21 .[#707070].> 4 : <<XML/HTTPS>>\nUżywa
20 .[#707070].> 11 : <<JDBC>>\nCzyta i zapisuje do
18 .[#707070].> 22 : <<SYNC>>\nUżywa
17 .[#707070].> 20 : <<SYNC>>\nUżywa
19 .[#707070].> 21 : <<SYNC>>\nUżywa
19 .[#707070].> 20 : <<SYNC>>\nUżywa
@enduml
```

to see generated charts
