# lab_de Short — Quellen je Satz

Stand: 2026-11-01. Jede Zeile der Vertonung (tts.py, SCRIPTS["lab_de"]) mit Quelle und Prüfstatus.
Legende: **GEPRÜFT** = Originaltext im Browser abgerufen und Zitat geprüft. **SUCHE** = nur Suchergebnis-Ausschnitt gesehen, Seite nicht geöffnet.

| # | Satz (Video) | Quelle | Status | Wörtlich / Anmerkung |
|---|---|---|---|---|
| 1 | Auf Käse steht das Lab oft nicht. | [legislation.gov.uk, Art. 19(1)(d) Verordnung (EU) 1169/2011](https://www.legislation.gov.uk/eur/2011/1169/article/19) | GEPRÜFT (UK-Kopie des EU-Texts) | "cheese, butter, fermented milk and cream, to which no ingredient has been added other than lactic products, food enzymes..." → Zutatenliste für Käse mit nur diesen Zusätzen nicht vorgeschrieben. "oft" bewusst gewählt. EUR-Lex konnte nicht abgerufen werden. |
| 2 | Lab ist das Enzym, das Milch zum Gerinnen bringt. | [Utopia: Tierisches Lab](https://utopia.de/ratgeber/tierisches-lab-was-der-begriff-bei-kaese-bedeutet/) | GEPRÜFT | "Diese speziellen Enzyme spalten das Milcheiweiß, sodass die Milch eindickt." |
| 3 | Es kommt aus Kälbermägen oder von Mikroben. | [Utopia: Tierisches Lab](https://utopia.de/ratgeber/tierisches-lab-was-der-begriff-bei-kaese-bedeutet/), [Utopia: Mikrobielles Lab](https://utopia.de/ratgeber/mikrobielles-lab-was-die-bezeichnung-bei-kaese-bedeutet/) | GEPRÜFT | Tierisches Lab: "das Lab aus den Mägen geschlachteter Kälber stammt". Mikrobielles Lab: "entsteht im Labor während der Fermentation von Schimmelpilzkulturen". Gentechnisches Chymosin im Video nicht erwähnt. |
| 4 | Mikrobielles Lab ist laut Diyanet unbedenklich. | [Diyanet DİYK Fetva: Peynir mayası](https://kurul.diyanet.gov.tr/tr/fetva/peynir-yapiminda-kullanilan-mayalarin-dini-hukmu-nedir/0193c42d-aa60-7126-92d8-9c538f6b513b) | GEPRÜFT | "Bitkisel ve mikrobiyal mayalar ... kullanılmasında ... dinî açıdan bir sakınca bulunmamaktadır." Deutsch: "religiös unbedenklich". |
| 5 | Tierisches Lab ist umstritten. Die Gelehrten sind sich uneinig. | [Diyanet Fetva](https://kurul.diyanet.gov.tr/tr/fetva/peynir-yapiminda-kullanilan-mayalarin-dini-hukmu-nedir/0193c42d-aa60-7126-92d8-9c538f6b513b) (Diyanet: erlaubtes Tier + keine Schweinemaga → caiz); [IslamOnline: Halal rennet ruling](https://fiqh.islamonline.net/en/halal-rennet-ruling-on-cheese-and-enzymes/) und [Muftionline: Hanafi-Meinung](https://muftionline.co.za/node/36843) (Mehrheit: unrein; Hanafi uneinig) | GEPRÜFT (Diyanet, IslamOnline); Hanefi-Teil SUCHE (Muftionline) | IslamOnline: "the majority of scholars view rennet from an animal not slaughtered Islamically as impure and prohibited." Diyanet: erlaubt von erlaubten Tieren, Schweinelab nicht. Die Mehrheits- und die Diyanet-Position weichen ab, daher "umstritten" korrekt. IslamOnline erwähnt die Hanefi-Meinung NICHT; das Video sagt deshalb nichts über Hanefi. |
| 6 | Halal Checker markiert es und erklärt es nach deinem Madhhab. | App (HalalAnalyzer) | GEPRÜFT im Emulator (Standard): Milbona 20692490 → DOUBTFUL. Hanefi-Verhalten: nur per Unit-Test im Repo (HalalAnalyzerMadhhabOverrideTest). | Keine Aussage "ist haram". |
| 7 | Kostenlos: Scan mit Halal Checker. | Play Store: com.fatihgulen.helalrehberi | GEPRÜFT (App im Play Store) | Link mit utm_campaign=lab_de in der Beschreibung. |

## Nicht im Video (bewusst)
- Keine Aussage über Schweinelab-Anteil, keine Prozentzahlen (80–95 % sind US-Zahlen aus einer Fatwa-Seite, nicht geprüft).
- Keine Behauptung, dass ein bestimmtes Produkt halal oder haram ist.
- Keine Gentechnik-Aussage.

## Nachprüfung (2026-11-01)
1. **Muftionline** (node/36843) geöffnet: Hanefi-Position ist gespalten. Abu Hanifa: erlaubt (altes Verfahren). Abu Yusuf und Muhammad: unerlaubt. Empfehlung: "Vorsicht" (Zitat: "it is safest to refrain on account of the difference of opinion"). Für heutiges tierisches Lab nennt die Seite es "impermissible according to all our imaams". Das Video sagt nur "umstritten" und ist damit eine vorsichtige Vereinfachung. Sie ist nicht falsch, aber nicht vollständig. Bei Hanefi wäre "heute meist als unerlaubt eingestuft" die genauere Formulierung. Diese Abweichung in der Videobeschreibung klarstellen, falls ein Korrekturhinweis nötig ist.
2. **App im Emulator (Hanefi gewählt, Chip zeigt "Hanafi")**: Milbona 20692490 → DOUBTFUL / MUSHBOOH, Grund "Animal Rennet / Cheese Enzymes". Das stimmt mit Satz 6 überein: die App markiert es und erklärt es. Am Ergebnis ändert sich unter Hanefi nichts; "nach deinem Madhhab" meint hier die Erklärung und die Regeln, bei denen die Schulen auseinandergehen.

## Offen
- YouTube: Etiketten, Sprache, EN-Übersetzung, DE/EN-Untertitel prüfen (YOUTUBE_CHECKLIST.md).
