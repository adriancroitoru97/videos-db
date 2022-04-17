# Programare Orientata pe Obiecte - VideosDB

O platforma care simuleaza actiuni pe care le pot face utilizatorii pe o
platforma de vizualizare de filme: ratings, vizualizare film, cautari,
recomandari, etc.

## Design-ul implementarii

### Database
Se initializeaza cu informatiile primite din fisierele JSON de input - lista
de show-uri (filme sau seriale), lista de actori si lista de useri ai
platformei. Tot la acest pas se traseaza legaturile dintre clase (spre exemplu
fiecare actor contine o lista de obiecte Show - filmografia acestuia).
S-au preferat legaturile directe intre obiecte si nu doar o lista de
String-uri, pentru a facilita operatiile viitoare pe database.

### ActionsHandler
Se preia fiecare operatie din input si se redirectioneaza executia programului,
in functie de tipul operatiei, catre una din cele 3 clase executoare de
operatii - Commands, Queries, Recommendations.

#### Commands
Contine cate o metoda pentru fiecare dintre cele 3 tipuri de comenzi.
Se folosesc link-urile facute inca din database dintre useri, actori si
show-uri pentru a se parcurge usor datele si a se efectua modificarile.
Se folosesc metode din clasa Utils pentru a converti un String la User, Actor,
Genre, dar si metode private pentru a se determina posibilitatea
aplicarii comenzii.

#### Queries
Contine 3 metode generice, care vor rezolva query-urile legate de useri, actori
si show-uri. Genericitatea este data de faptul ca filtrele, sortarile si
limitarile impuse respectivului query folosesc, relativ, aceleasi instructiuni.
Particularizarea pe fiecare caz este facuta prin asignarea manuala a
comparatorilor folositi pentru sortarea crescatoare/descrescatoare, fiecare tip
de query bazandu-se pe propriul criteriu de sortare. Este folosita clasa Stream
pentru operatiile pe listele de useri/actori/shows.

#### Recommendations
Contine metode pentru fiecare tip de recomandare, dar si metode private care
determina posibilitatea returnarii unei recomandari. Se folosesc stream-urile
pentru a aplica toate conditiile necesare returnarii recomandarii optime.

### MessageWriter
Clasa de metode care se apeleaza in cadrul fiecarei operatii si printeaza in
JSONArray -ul de output mesajul corespunzator efectuarii (cu succes sau nu) a
fiecarei operatii. Se folosesc atat legaturile dintre clase pentru o accesare
rapida a informatiilor cautate, dar si operatiile cu Stream-uri pentru a
returna recomandarea ceruta. 

## Flow
In cadrul majoritatii operatiilor, se fac transformari din String
(cum este oferit input-ul) in Actor/User/Show, intrucat database-ul este
construit cu referinte directe la aceste obiecte, pentru a facilita accesul
rapid si interoperabilitatea. Se realizeaza parcurgeri si accesari ale
listelor/map-urilor de astfel de obiecte pentru a se efectua diverse operatii.

Sistemul de ActionsHandler este conceput pe baza unor clase abastracte ce
implementeaza metode statice. Fiind clase de metode, nu este necesara o
instantiere a acestora.

Operatiile nu se introduc in database, intrucat informatiile aferente acestora
sunt accesate strict la momentul executiei respectivei operatii, spre deosebire
de celelalte campuri din database, pe care se realizeaza intreaga platforma.

## License
[Adrian-Valeriu Croitoru](https://github.com/adriancroitoru97)
