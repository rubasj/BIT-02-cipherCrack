Kryptoanalýza

Při spuštění je potřeba napsat jako argument příkazové řádky soubor, kde se nachází šifry.
Každá načtená šifra je nejdříve porovnávána pomocí indexu shody, který určí pravděpodobnost, zda se jedná
o monoalfabetickou, popřípadě polyalfabetickou šifru.

Pokud se jedná o monoalfabetickou šifru, nejprve dojde ke kontrole, zda je tato šifra delší než 500 znaků,

Pokud je šifra kratší, automaticky se začne analyzovat pomocí Caesara a frekvenční analýzy dle četnosti malých
znaků.
Jakožto 500 znaků jsem zvolil, protože si nemyslím, že by s nějakým dobrým výsedkem šlo realizovat frekvenční analýzu
pomocí bigramů, trigramů a quadrigramů.


###################################################################
Ceasarova šifra

Tuto šifru jsem analyzoval dle četnosti písmen. Vychází se z písmene s nejdelší četností, zjistí se posun a dojde k rozšifrování.
Nejčetnější písmeno se porovnává ještě s druhým nejčetnějším.

Vzhledem k tomu, že brute force zde není příliš náročný v této šifře, pokusil jsem se oněj taky. Dochází zde pouze k 26 porovnání výsledku.

Vzhledem k tomu, že šifra není rozdělena na slova dle velikosti, zvolil jsem strategii odstranit všechny mezery a výsledek
pouze porovnat se slovníkem, zda alespoň obsahuje slovo. Dalším faktorem je to, že šifra je krátká.


###################################################################
Frekvenční analýza ngram

Při větší šifře (>500) dochází k analýze s ngramy.

Jako strategii jsem nejdříve zvolil porovnání četností trigramů, protože je zde menší pravděpodobnost, že se první dva nejčastěji používané
zamění.

Princip:
    Nalezení čtyř nejčetnějších a následná změna všech znaků, které zahrnují. Dojde-li k nějaké duplicitě (dva znaky lze zaměnit za jeden)
    program porovná aktuální stav dešifrovaného textu (celá, již dešifrovaná slova) a vyhodnotí, který znak se hodí více.

    Následně se porovnají zbylé používané trigramy v AJ, v případech duplicity se také porovná. Porovnají se pouze ve chvíli, kdy trigram v textu
    obsahuje maximálně jedno nerozšifrované slovo.

    Dále se porovnají i bigramy a quadrigramy podobnou cestou. Nakonec dojde k opětovnému porovnání s trigramy a bigramy, pro případ, že nějaký bigram  je možno
    doplnit.

    Nakonec dojde k frekvenční analýze samostatných, neobsažených písmen.

   Z mého pohledu nejnáročnější na realizaci.

##########################################################################
Analýza polyalfabetické šifry

Po spuštění analýzy dojde nejprve hledání vícenásobně se vyskytujících sekvencí a hledání vzdáleností.
Následně se vyhodnotí pomocí nejčastějšího dělitele těchto vzdáleností délka klíče.

Po zjištění délky klíče dojde k frekvenční analýze jednotlivých abeced klíče.
Po zjištění posunů jednotlivých abeced dojde k rozšifrování a zjištění klíče. Klíč by měl být BIDEN.