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

    // Lead-in phrase for the status banner subtitle when we have the actual flagged ingredient
    // name(s) to append (e.g. "Contains: E471 Mono- and Diglycerides"). Used instead of
    // getStatusCardSubtitle's generic per-status text so the banner names what was really found
    // in *this* product rather than a fixed example compound (previously always "E471"/"E120"
    // regardless of what actually triggered the flag).
    fun getSubtitleContainsPrefix(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Contains"
        AppLanguage.DE -> "Enthält"
        AppLanguage.FR -> "Contient"
        AppLanguage.TR -> "İçeriyor"
        AppLanguage.AR -> "يحتوي على"
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

    fun getOriginLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Source"
        AppLanguage.DE -> "Herkunft"
        AppLanguage.FR -> "Origine"
        AppLanguage.TR -> "Kaynak"
        AppLanguage.AR -> "المصدر"
    }

    fun getAlternateNamesLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Alternative Names: "
        AppLanguage.DE -> "Alternative Namen: "
        AppLanguage.FR -> "Noms alternatifs : "
        AppLanguage.TR -> "Alternatif İsimler: "
        AppLanguage.AR -> "أسماء بديلة: "
    }

    fun getCommonUsageLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Common Usage"
        AppLanguage.DE -> "Verwendung"
        AppLanguage.FR -> "Utilisation courante"
        AppLanguage.TR -> "Kullanım Alanı"
        AppLanguage.AR -> "الاستخدام الشائع"
    }

    fun getCameraPermissionError(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Camera unavailable"
        AppLanguage.DE -> "Kamera nicht verfügbar"
        AppLanguage.FR -> "Caméra indisponible"
        AppLanguage.TR -> "Kameraya erişilemedi"
        AppLanguage.AR -> "لا يمكن الوصول إلى الكاميرا"
    }

    fun getCameraPermissionErrorDetail(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Camera permission was not granted, or the camera may be in use by another app. Please check permissions and try again."
        AppLanguage.DE -> "Kamerazugriff wurde nicht gewährt oder die Kamera wird von einer anderen App verwendet. Bitte Berechtigungen prüfen und erneut versuchen."
        AppLanguage.FR -> "L'autorisation de la caméra n'a pas été accordée, ou la caméra est peut-être utilisée par une autre application. Veuillez vérifier les autorisations et réessayer."
        AppLanguage.TR -> "Kamera izni verilmemiş veya kamera başka bir uygulama tarafından kullanılıyor olabilir. Lütfen izinleri kontrol edip tekrar deneyin."
        AppLanguage.AR -> "لم يتم منح إذن الكاميرا، أو قد تكون الكاميرا قيد الاستخدام من قبل تطبيق آخر. يرجى التحقق من الأذونات والمحاولة مرة أخرى."
    }

    fun getBackButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Back"
        AppLanguage.DE -> "Zurück"
        AppLanguage.FR -> "Retour"
        AppLanguage.TR -> "Geri Dön"
        AppLanguage.AR -> "رجوع"
    }

    fun getBarcodeScannerTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Barcode Scanner"
        AppLanguage.DE -> "Barcode-Scanner"
        AppLanguage.FR -> "Scanner de code-barres"
        AppLanguage.TR -> "Barkod Tarayıcı"
        AppLanguage.AR -> "ماسح الباركود"
    }

    fun getFlash(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Flash"
        AppLanguage.DE -> "Blitz"
        AppLanguage.FR -> "Flash"
        AppLanguage.TR -> "Flaş"
        AppLanguage.AR -> "الفلاش"
    }

    fun getAlignBarcode(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Align the barcode within the frame"
        AppLanguage.DE -> "Barcode im Rahmen ausrichten"
        AppLanguage.FR -> "Alignez le code-barres dans le cadre"
        AppLanguage.TR -> "Barkodu çerçevenin ortasına hizalayın"
        AppLanguage.AR -> "قم بمحاذاة الباركود داخل الإطار"
    }

    fun getAutoScanHint(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "It will be scanned and looked up automatically"
        AppLanguage.DE -> "Wird automatisch gescannt und nachgeschlagen"
        AppLanguage.FR -> "Il sera scanné et recherché automatiquement"
        AppLanguage.TR -> "Otomatik olarak taranıp veritabanından sorgulanacaktır"
        AppLanguage.AR -> "سيتم مسحه والبحث عنه تلقائياً"
    }

    fun getEnterManually(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Enter Barcode Manually"
        AppLanguage.DE -> "Barcode manuell eingeben"
        AppLanguage.FR -> "Saisir le code-barres"
        AppLanguage.TR -> "Barkodu Elle Gir"
        AppLanguage.AR -> "إدخال الباركود يدوياً"
    }

    fun getShareSubjectPrefix(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Halal Food Check"
        AppLanguage.DE -> "Halal Lebensmittelprüfung"
        AppLanguage.FR -> "Contrôle alimentaire Halal"
        AppLanguage.TR -> "Helal Gıda Kontrolü"
        AppLanguage.AR -> "فحص الطعام الحلال"
    }

    fun getShareResultHeader(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Halal Food Check Result"
        AppLanguage.DE -> "Ergebnis der Halal-Prüfung"
        AppLanguage.FR -> "Résultat du contrôle Halal"
        AppLanguage.TR -> "Helal Gıda Kontrol Sonucu"
        AppLanguage.AR -> "نتيجة فحص الطعام الحلال"
    }

    fun getShareProductLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Product"
        AppLanguage.DE -> "Produkt"
        AppLanguage.FR -> "Produit"
        AppLanguage.TR -> "Ürün"
        AppLanguage.AR -> "المنتج"
    }

    fun getShareBarcodeLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Barcode"
        AppLanguage.DE -> "Barcode"
        AppLanguage.FR -> "Code-barres"
        AppLanguage.TR -> "Barkod"
        AppLanguage.AR -> "الباركود"
    }

    fun getShareStatusLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Status"
        AppLanguage.DE -> "Status"
        AppLanguage.FR -> "Statut"
        AppLanguage.TR -> "Durum"
        AppLanguage.AR -> "الحالة"
    }

    fun getShareFlaggedIngredientsLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Flagged/Suspicious Ingredients"
        AppLanguage.DE -> "Bedenkliche/Zweifelhafte Inhaltsstoffe"
        AppLanguage.FR -> "Ingrédients problématiques/douteux"
        AppLanguage.TR -> "Sakıncalı/Şüpheli Maddeler"
        AppLanguage.AR -> "مكونات محظورة/مشبوهة"
    }

    fun getShareChooserTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Share Result"
        AppLanguage.DE -> "Ergebnis teilen"
        AppLanguage.FR -> "Partager le résultat"
        AppLanguage.TR -> "Sonucu Paylaş"
        AppLanguage.AR -> "مشاركة النتيجة"
    }

    fun getReportError(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Report Error"
        AppLanguage.DE -> "Fehler melden"
        AppLanguage.FR -> "Signaler une erreur"
        AppLanguage.TR -> "Hata Bildir"
        AppLanguage.AR -> "الإبلاغ عن خطأ"
    }

    fun getReportErrorEmailSubject(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Incorrect Result Report"
        AppLanguage.DE -> "Meldung eines falschen Ergebnisses"
        AppLanguage.FR -> "Signalement d'un résultat incorrect"
        AppLanguage.TR -> "Hatalı Sonuç Bildirimi"
        AppLanguage.AR -> "الإبلاغ عن نتيجة غير صحيحة"
    }

    fun getReportErrorPromptLine(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Please describe what's incorrect and what the correct classification should be:"
        AppLanguage.DE -> "Bitte beschreiben Sie, was falsch ist und wie die richtige Einstufung lauten sollte:"
        AppLanguage.FR -> "Merci de décrire ce qui est incorrect et quelle devrait être la bonne classification :"
        AppLanguage.TR -> "Lütfen neyin yanlış olduğunu ve doğru sınıflandırmanın ne olması gerektiğini açıklayın:"
        AppLanguage.AR -> "يرجى وصف الخطأ وما ينبغي أن يكون التصنيف الصحيح:"
    }

    fun getNoEmailAppFound(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "No email app found. Please write to"
        AppLanguage.DE -> "Keine E-Mail-App gefunden. Bitte schreiben Sie an"
        AppLanguage.FR -> "Aucune application e-mail trouvée. Merci d'écrire à"
        AppLanguage.TR -> "E-posta uygulaması bulunamadı. Lütfen şu adrese yazın"
        AppLanguage.AR -> "لم يتم العثور على تطبيق بريد إلكتروني. يرجى الكتابة إلى"
    }

    fun getImageLoadError(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Failed to load image"
        AppLanguage.DE -> "Bild konnte nicht geladen werden"
        AppLanguage.FR -> "Échec du chargement de l'image"
        AppLanguage.TR -> "Görsel yüklenemedi"
        AppLanguage.AR -> "فشل تحميل الصورة"
    }

    fun getUnknownError(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "unknown error"
        AppLanguage.DE -> "unbekannter Fehler"
        AppLanguage.FR -> "erreur inconnue"
        AppLanguage.TR -> "bilinmeyen hata"
        AppLanguage.AR -> "خطأ غير معروف"
    }

    fun getUnexpectedError(lang: AppLanguage, details: String): String = when (lang) {
        AppLanguage.EN -> "An unexpected error occurred: $details. Please try again."
        AppLanguage.DE -> "Ein unerwarteter Fehler ist aufgetreten: $details. Bitte versuchen Sie es erneut."
        AppLanguage.FR -> "Une erreur inattendue s'est produite : $details. Veuillez réessayer."
        AppLanguage.TR -> "Beklenmeyen bir hata oluştu: $details. Lütfen tekrar deneyin."
        AppLanguage.AR -> "حدث خطأ غير متوقع: $details. يرجى المحاولة مرة أخرى."
    }

    fun getReasonLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Reason"
        AppLanguage.DE -> "Grund"
        AppLanguage.FR -> "Raison"
        AppLanguage.TR -> "Sebep"
        AppLanguage.AR -> "السبب"
    }

    fun getOnDeviceStorage(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "ON-DEVICE STORAGE"
        AppLanguage.DE -> "LOKALER SPEICHER"
        AppLanguage.FR -> "STOCKAGE LOCAL"
        AppLanguage.TR -> "CİHAZ ÜZERİNDE SAKLAMA"
        AppLanguage.AR -> "التخزين على الجهاز"
    }

    fun getItemsSuffix(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Items"
        AppLanguage.DE -> "Einträge"
        AppLanguage.FR -> "Éléments"
        AppLanguage.TR -> "Öğe"
        AppLanguage.AR -> "عناصر"
    }

    fun getChronologicalHistory(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "CHRONOLOGICAL HISTORY (NEWEST FIRST)"
        AppLanguage.DE -> "CHRONOLOGISCHER VERLAUF (NEUESTE ZUERST)"
        AppLanguage.FR -> "HISTORIQUE CHRONOLOGIQUE (PLUS RÉCENT D'ABORD)"
        AppLanguage.TR -> "KRONOLOJİK GEÇMİŞ (EN YENİ ÖNCE)"
        AppLanguage.AR -> "السجل الزمني (الأحدث أولاً)"
    }

    fun getFlaggedLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Flagged"
        AppLanguage.DE -> "Markiert"
        AppLanguage.FR -> "Signalé"
        AppLanguage.TR -> "İşaretlenen"
        AppLanguage.AR -> "تم الإبلاغ عنه"
    }

    fun getCertLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Cert"
        AppLanguage.DE -> "Zertifikat"
        AppLanguage.FR -> "Certif."
        AppLanguage.TR -> "Sertifika"
        AppLanguage.AR -> "شهادة"
    }

    fun getBarcodeNotFoundInOff(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Barcode not found in Open Food Facts."
        AppLanguage.DE -> "Barcode nicht in Open Food Facts gefunden."
        AppLanguage.FR -> "Code-barres introuvable dans Open Food Facts."
        AppLanguage.TR -> "Barkod Open Food Facts'ta bulunamadı."
        AppLanguage.AR -> "لم يتم العثور على الباركود في Open Food Facts."
    }

    fun getProhibitedLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Prohibited"
        AppLanguage.DE -> "Verboten"
        AppLanguage.FR -> "Interdit"
        AppLanguage.TR -> "Yasaklı"
        AppLanguage.AR -> "محظور"
    }

    fun getDoubtfulLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Doubtful"
        AppLanguage.DE -> "Zweifelhaft"
        AppLanguage.FR -> "Douteux"
        AppLanguage.TR -> "Şüpheli"
        AppLanguage.AR -> "مشبوه"
    }

    fun getCertificationLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Certification"
        AppLanguage.DE -> "Zertifizierung"
        AppLanguage.FR -> "Certification"
        AppLanguage.TR -> "Sertifikasyon"
        AppLanguage.AR -> "الشهادة"
    }

    fun getNoProhibitedAdditives(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "No prohibited additives. Safe and verified."
        AppLanguage.DE -> "Keine verbotenen Zusatzstoffe. Sicher und geprüft."
        AppLanguage.FR -> "Aucun additif interdit. Sûr et vérifié."
        AppLanguage.TR -> "Yasaklı katkı maddesi yok. Güvenli ve doğrulanmış."
        AppLanguage.AR -> "لا توجد إضافات محظورة. آمن وموثق."
    }

    fun getContainsNonHalalDerivatives(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Contains non-halal animal or alcohol derivatives."
        AppLanguage.DE -> "Enthält nicht-halal tierische oder alkoholische Bestandteile."
        AppLanguage.FR -> "Contient des dérivés animaux ou d'alcool non halal."
        AppLanguage.TR -> "Helal olmayan hayvansal veya alkol türevleri içerir."
        AppLanguage.AR -> "يحتوي على مشتقات حيوانية أو كحولية غير حلال."
    }

    fun getContainsUnverifiedAdditives(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Contains additives of unverified origin."
        AppLanguage.DE -> "Enthält Zusatzstoffe mit unklarer Herkunft."
        AppLanguage.FR -> "Contient des additifs d'origine non vérifiée."
        AppLanguage.TR -> "Kaynağı doğrulanmamış katkı maddeleri içerir."
        AppLanguage.AR -> "يحتوي على إضافات غير مؤكدة المصدر."
    }

    fun getClose(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Close"
        AppLanguage.DE -> "Schließen"
        AppLanguage.FR -> "Fermer"
        AppLanguage.TR -> "Kapat"
        AppLanguage.AR -> "إغلاق"
    }

    fun getSearch(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Search"
        AppLanguage.DE -> "Suchen"
        AppLanguage.FR -> "Rechercher"
        AppLanguage.TR -> "Ara"
        AppLanguage.AR -> "بحث"
    }

    fun getClear(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Clear"
        AppLanguage.DE -> "Löschen"
        AppLanguage.FR -> "Effacer"
        AppLanguage.TR -> "Temizle"
        AppLanguage.AR -> "مسح"
    }

    fun getClearAll(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Clear All"
        AppLanguage.DE -> "Alle löschen"
        AppLanguage.FR -> "Tout effacer"
        AppLanguage.TR -> "Tümünü Temizle"
        AppLanguage.AR -> "مسح الكل"
    }

    fun getSelected(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Selected"
        AppLanguage.DE -> "Ausgewählt"
        AppLanguage.FR -> "Sélectionné"
        AppLanguage.TR -> "Seçili"
        AppLanguage.AR -> "محدد"
    }

    fun getDetails(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Details"
        AppLanguage.DE -> "Details"
        AppLanguage.FR -> "Détails"
        AppLanguage.TR -> "Detaylar"
        AppLanguage.AR -> "التفاصيل"
    }

    fun getShare(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Share"
        AppLanguage.DE -> "Teilen"
        AppLanguage.FR -> "Partager"
        AppLanguage.TR -> "Paylaş"
        AppLanguage.AR -> "مشاركة"
    }

    fun getLanguageSelection(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> "Select Language"
        AppLanguage.DE -> "Sprache wählen"
        AppLanguage.FR -> "Choisir la langue"
        AppLanguage.TR -> "Dil Seçimi"
        AppLanguage.AR -> "اختر اللغة"
    }
}
