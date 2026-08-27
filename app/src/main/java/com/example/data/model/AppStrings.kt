package com.example.data.model

object AppStrings {

    fun getAppName(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Halal Checker"
        AppLanguage.DE -> "Halal Prüfer"
        AppLanguage.FR -> "Contrôle Halal"
        AppLanguage.TR -> "Helal Rehberi"
        AppLanguage.AR -> "دليل الحلال"
    }

    fun getScanBarcode(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Scan Barcode"
        AppLanguage.DE -> "Barcode Scannen"
        AppLanguage.FR -> "Scanner le code"
        AppLanguage.TR -> "Barkod Okut"
        AppLanguage.AR -> "مسح الباركود"
    }

    fun getScanSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Verify products from anywhere in the world"
        AppLanguage.DE -> "Produkte weltweit in Sekunden prüfen"
        AppLanguage.FR -> "Vérifiez des produits du monde entier"
        AppLanguage.TR -> "Dünya genelindeki ürünleri anında sorgulayın"
        AppLanguage.AR -> "تحقق من منتجات من جميع أنحاء العالم"
    }

    fun getQuickTestTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "SUGGESTED PRODUCTS"
        AppLanguage.DE -> "VORGESCHLAGENE PRODUKTE"
        AppLanguage.FR -> "PRODUITS SUGGÉRÉS"
        AppLanguage.TR -> "ÖNE ÇIKAN ÜRÜNLER"
        AppLanguage.AR -> "منتجات مقترحة"
    }

    fun getTotalScans(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Total Scans"
        AppLanguage.DE -> "Gesamt Scans"
        AppLanguage.FR -> "Total Scans"
        AppLanguage.TR -> "Toplam Tarama"
        AppLanguage.AR -> "إجمالي الفحوصات"
    }

    fun getHalalProducts(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Halal"
        AppLanguage.DE -> "Halal"
        AppLanguage.FR -> "Halal"
        AppLanguage.TR -> "Helal"
        AppLanguage.AR -> "حلال"
    }

    fun getHaramOrDoubtful(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Haram / Doubtful"
        AppLanguage.DE -> "Haram / Zweifelhaft"
        AppLanguage.FR -> "Haram / Douteux"
        AppLanguage.TR -> "Haram / Şüpheli"
        AppLanguage.AR -> "حرام / مشبوه"
    }

    fun getRecentScans(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "RECENT SCANS"
        AppLanguage.DE -> "LETZTE SCANS"
        AppLanguage.FR -> "SCANS RÉCENTS"
        AppLanguage.TR -> "SON SORGULAMALAR"
        AppLanguage.AR -> "الفحوصات الأخيرة"
    }

    fun getAll(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "All"
        AppLanguage.DE -> "Alle"
        AppLanguage.FR -> "Tous"
        AppLanguage.TR -> "Tümü"
        AppLanguage.AR -> "الكل"
    }

    fun getStatusLabel(status: HalalStatus, lang: AppLanguage): String = when (status) {
        HalalStatus.HELAL -> when (lang) {
            AppLanguage.EN -> "HALAL"
            AppLanguage.DE -> "HALAL"
            AppLanguage.FR -> "HALAL"
            AppLanguage.TR -> "HELAL"
            AppLanguage.AR -> "حلال"
        }
        HalalStatus.HARAM -> when (lang) {
            AppLanguage.EN -> "HARAM"
            AppLanguage.DE -> "HARAM"
            AppLanguage.FR -> "HARAM"
            AppLanguage.TR -> "HARAM"
            AppLanguage.AR -> "حرام"
        }
        HalalStatus.SUPHELI -> when (lang) {
            AppLanguage.EN -> "DOUBTFUL"
            AppLanguage.DE -> "ZWEIFELHAFT"
            AppLanguage.FR -> "DOUTEUX"
            AppLanguage.TR -> "ŞÜPHELİ"
            AppLanguage.AR -> "مشبوه"
        }
        HalalStatus.BULUNAMADI -> when (lang) {
            AppLanguage.EN -> "NOT FOUND"
            AppLanguage.DE -> "NICHT GEFUNDEN"
            AppLanguage.FR -> "NON TROUVÉ"
            AppLanguage.TR -> "BULUNAMADI"
            AppLanguage.AR -> "غير موجود"
        }
    }

    fun getStatusCardTitle(status: HalalStatus, lang: AppLanguage): String = when (status) {
        HalalStatus.HELAL -> when (lang) {
            AppLanguage.EN -> "NO FLAGGED INGREDIENTS"
            AppLanguage.DE -> "KEINE BEDENKLICHEN ZUTATEN"
            AppLanguage.FR -> "AUCUN INGRÉDIENT PROBLÉMATIQUE"
            AppLanguage.TR -> "SAKINCALI MADDE BULUNAMADI"
            AppLanguage.AR -> "لا توجد مكونات مثيرة للقلق"
        }
        HalalStatus.HARAM -> when (lang) {
            AppLanguage.EN -> "HARAM - PROHIBITED"
            AppLanguage.DE -> "HARAM - UNZULÄSSIG"
            AppLanguage.FR -> "HARAM - INTERDIT"
            AppLanguage.TR -> "HARAM ÜRÜN"
            AppLanguage.AR -> "حرام - غير مباح"
        }
        HalalStatus.SUPHELI -> when (lang) {
            AppLanguage.EN -> "DOUBTFUL / MUSHBOOH"
            AppLanguage.DE -> "ZWEIFELHAFTE ZUTATEN"
            AppLanguage.FR -> "INGRÉDIENTS DOUTEUX"
            AppLanguage.TR -> "ŞÜPHELİ ÜRÜN"
            AppLanguage.AR -> "مشبوه - يرجى الحذر"
        }
        HalalStatus.BULUNAMADI -> when (lang) {
            AppLanguage.EN -> "NO INGREDIENTS RECORD"
            AppLanguage.DE -> "KEINE ZUTATENLISTE"
            AppLanguage.FR -> "AUCUNE DONNÉE D'INGRÉDIENTS"
            AppLanguage.TR -> "İÇERİK BULUNAMADI"
            AppLanguage.AR -> "لا توجد قائمة مكونات"
        }
    }

    fun getStatusCardSubtitle(status: HalalStatus, lang: AppLanguage): String = when (status) {
        HalalStatus.HELAL -> when (lang) {
            AppLanguage.EN -> "Safe for Muslim consumption. Free of pork and non-halal animal additives."
            AppLanguage.DE -> "Frei von Schweinefleisch, Alkohol und nicht-halal tierischen Zusätzen."
            AppLanguage.FR -> "Sans porc, alcool ni dérivés animaux non autorisés."
            AppLanguage.TR -> "Güvenle tüketebilirsiniz. Domuz, alkol ve sakıncalı katkı içermez."
            AppLanguage.AR -> "خالٍ من مشتقات الخنزير والكحول والإضافات المحرمة."
        }
        HalalStatus.HARAM -> when (lang) {
            AppLanguage.EN -> "Contains prohibited ingredients (Pork derivatives, alcohol, or insect carmine)."
            AppLanguage.DE -> "Enthält verbotene Inhaltsstoffe (Schweinegelatine, Alkohol, Karmin E120)."
            AppLanguage.FR -> "Contient des ingrédients interdits (Porc, gélatine porcine, alcool, carmin E120)."
            AppLanguage.TR -> "Tüketilmesi uygun değildir! Sakıncalı maddeler (Domuz, alkol, E120) tespit edildi."
            AppLanguage.AR -> "يحتوي على مكونات محرمة (مشتقات الخنزير، كحول، أو كارمين E120)."
        }
        HalalStatus.SUPHELI -> when (lang) {
            AppLanguage.EN -> "Contains additives of unverified origin (Animal vs. Plant mono/diglycerides, uncertified enzymes)."
            AppLanguage.DE -> "Enthält Zusätze mit unklarer Herkunft (Tierische vs. Pflanzliche Emulgatoren E471)."
            AppLanguage.FR -> "Contient des additifs d'origine inconnue (émulsifiants E471, présure)."
            AppLanguage.TR -> "Katkı maddelerinin kaynağı belirsizdir (Hayvansal/Bitkisel yağ emülgatörleri E471)."
            AppLanguage.AR -> "يحتوي على إضافات غير مؤكدة المصدر (نباتي أو حيواني مثل E471)."
        }
        HalalStatus.BULUNAMADI -> when (lang) {
            AppLanguage.EN -> "Barcode found in Open Food Facts, but ingredients text is empty. Please inspect the label."
            AppLanguage.DE -> "Produkt gefunden, aber Zutatenliste in Open Food Facts ist leer. Bitte Etikett prüfen."
            AppLanguage.FR -> "Code-barres indexé mais la liste d'ingrédients est vide. Vérifiez l'emballage."
            AppLanguage.TR -> "Ürün kaydı mevcut fakat içindekiler listesi henüz girilmemiş. Lütfen paketi inceleyin."
            AppLanguage.AR -> "المنتج مسجل ولكن قائمة المكونات فارغة، يرجى فحص الغلاف."
        }
    }

    fun getProhibitedIngredientsHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Prohibited / Flagged Ingredients"
        AppLanguage.DE -> "Verbotene / Riskante Inhaltsstoffe"
        AppLanguage.FR -> "Ingrédients interdits détectés"
        AppLanguage.TR -> "İçerikteki Sakıncalı / Haram Maddeler"
        AppLanguage.AR -> "المكونات المحظورة أو المشبوهة"
    }

    fun getSuspiciousIngredientsHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Doubtful Ingredients (Source Unverified)"
        AppLanguage.DE -> "Zweifelhafte Zusatzstoffe (Herkunft unklar)"
        AppLanguage.FR -> "Additifs douteux (Origine non spécifiée)"
        AppLanguage.TR -> "Şüpheli Görülen Katkı Maddeleri"
        AppLanguage.AR -> "مكونات تحتاج للتحقق من المصدر"
    }

    fun getAnalysisReport(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Analysis & Verification Details"
        AppLanguage.DE -> "Prüfungsbericht & Details"
        AppLanguage.FR -> "Rapport d'analyse et détails"
        AppLanguage.TR -> "Kontrol Raporu ve Detaylar"
        AppLanguage.AR -> "تقرير التحليل والتفاصيل"
    }

    fun getHalalAlternatives(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Halal & Clean Alternatives"
        AppLanguage.DE -> "Halal & Saubere Alternativen"
        AppLanguage.FR -> "Alternatives Halal Recommandées"
        AppLanguage.TR -> "Helal ve Güvenli Alternatif Tavsiyeleri"
        AppLanguage.AR -> "بدائل حلال ونظيفة موصى بها"
    }

    fun getIngredientsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Product Ingredients"
        AppLanguage.DE -> "Zutatenliste"
        AppLanguage.FR -> "Ingrédients du produit"
        AppLanguage.TR -> "Ürün İçindekiler Listesi"
        AppLanguage.AR -> "مكونات المنتج"
    }

    fun getScanAgain(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Scan Another Product"
        AppLanguage.DE -> "Weiteres Produkt scannen"
        AppLanguage.FR -> "Scanner un autre produit"
        AppLanguage.TR -> "Tekrar Barkod Tara"
        AppLanguage.AR -> "مسح منتج آخر"
    }

    fun getManualBarcodeTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Manual Barcode Entry"
        AppLanguage.DE -> "Barcode manuell eingeben"
        AppLanguage.FR -> "Saisie manuelle du code-barres"
        AppLanguage.TR -> "Manuel Barkod Girişi"
        AppLanguage.AR -> "إدخال الباركود يدوياً"
    }

    fun getEAdditivesTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "E-Codes & Additives Guide"
        AppLanguage.DE -> "E-Nummern & Zusatzstoff-Recherche"
        AppLanguage.FR -> "Guide des Additifs & Codes E"
        AppLanguage.TR -> "E-Kodları & Katkı Rehberi"
        AppLanguage.AR -> "دليل الأرقام والإضافات E"
    }

    fun getHistoryTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Scan History"
        AppLanguage.DE -> "Scan-Verlauf"
        AppLanguage.FR -> "Historique des scans"
        AppLanguage.TR -> "Tarama Geçmişi"
        AppLanguage.AR -> "سجل الفحوصات"
    }

    fun getNavHome(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Home"
        AppLanguage.DE -> "Start"
        AppLanguage.FR -> "Accueil"
        AppLanguage.TR -> "Ana Sayfa"
        AppLanguage.AR -> "الرئيسية"
    }

    fun getNavHistory(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "History"
        AppLanguage.DE -> "Verlauf"
        AppLanguage.FR -> "Historique"
        AppLanguage.TR -> "Geçmiş"
        AppLanguage.AR -> "السجل"
    }

    fun getNavECodes(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "E-Codes"
        AppLanguage.DE -> "E-Nummern"
        AppLanguage.FR -> "Codes-E"
        AppLanguage.TR -> "E-Kodları"
        AppLanguage.AR -> "أكواد E"
    }

    fun getClearHistory(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Clear History"
        AppLanguage.DE -> "Verlauf löschen"
        AppLanguage.FR -> "Effacer l'historique"
        AppLanguage.TR -> "Geçmişi Temizle"
        AppLanguage.AR -> "مسح السجل"
    }

    fun getSearchPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Search product, brand or barcode..."
        AppLanguage.DE -> "Produkt, Marke oder Barcode suchen..."
        AppLanguage.FR -> "Rechercher produit, marque ou code..."
        AppLanguage.TR -> "Ürün, marka veya barkod ara..."
        AppLanguage.AR -> "ابحث عن منتج، علامة تجارية أو باركود..."
    }

    fun getLanguageSelection(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Select Language"
        AppLanguage.DE -> "Sprache wählen"
        AppLanguage.FR -> "Choisir la langue"
        AppLanguage.TR -> "Dil Seçimi"
        AppLanguage.AR -> "اختر اللغة"
    }
}
