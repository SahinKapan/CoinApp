CoinApp Doküman
1-) İlk önce XML’lerimizi oluşturup tasarıma göre düzenledim. (activity_detail.xml, activity_main.xml, item_layout.xml)
2-) Api’ye göre uygun model oluşturdum. Api’den gelen json şu şekilde:
{
"status": {
"timestamp": "2022-05-03T19:48:37.625Z", "error_code": 0,
"error_message": null,
"elapsed": 22,
"credit_count": 1, "notice": null, "total_count": 10082
}, "data": [
{
"id": 1,
"name": "Bitcoin",
"symbol": "BTC",
"slug": "bitcoin",
"num_market_pairs": 9382,
"date_added": "2013-04-28T00:00:00.000Z", "tags": [
"mineable",
"pow",
"sha-256",
"store-of-value", "state-channel", "coinbase-ventures-portfolio", "three-arrows-capital-portfolio", "polychain-capital-portfolio", "binance-labs-portfolio", "blockchain-capital-portfolio", "boostvc-portfolio", "cms-holdings-portfolio", "dcg-portfolio", "dragonfly-capital-portfolio", "electric-capital-portfolio", "fabric-ventures-portfolio", "framework-ventures-portfolio", "galaxy-digital-portfolio", "huobi-capital-portfolio", "alameda-research-portfolio", "a16z-portfolio", "1confirmation-portfolio", "winklevoss-capital-portfolio",
 "usv-portfolio", "placeholder-ventures-portfolio", "pantera-capital-portfolio", "multicoin-capital-portfolio", "paradigm-portfolio"
],
"max_supply": 21000000, "circulating_supply": 19029693, "total_supply": 19029693,
"platform": null,
"cmc_rank": 1, "self_reported_circulating_supply": null, "self_reported_market_cap": null, "last_updated": "2022-05-03T19:48:00.000Z", "quote": {
"USD": {
"price": 37600.16600479654,
"volume_24h": 26667545422.271423, "volume_change_24h": -19.8645, "percent_change_1h": -0.23138083, "percent_change_24h": -2.14725311, "percent_change_7d": -1.7796267, "percent_change_30d": -19.10871316, "percent_change_60d": -6.30699181, "percent_change_90d": -0.07214666, "market_cap": 715519615820.3147, "market_cap_dominance": 42.1518, "fully_diluted_market_cap": 789603486100.73, "last_updated": "2022-05-03T19:48:00.000Z"
} }
}, ...
3-) APIRequest interface’ni oluşturdum. Burada yer alan getCoins fonksiyonu default olarak gelen veriler ve sıralamadır. getCoinsWithRank fonksiyonu ise özel bir sıralama yapmak istersek çalışacak olan fonksiyondur.
4-) Bir sınıftan tek bir instance oluşmasını istediğim için RetrofitClient object’ini oluşturdum. Burada BASE_URL’imiz ve APIRequest instance’ımız mevcuttur.
5-) adapters package’nın içindeki CoinsAdapter’ı oluşturdum.
• Burada Data objelerimizi toplamak için ‘list’ i tanımladık.
• inner classımız içinde bind ve init var. bind fonksiyonu itemlar için oluşturduğumuz
item_layout’daki viewları gelen datalara bağlama işlemini üstleniyor. Bunu onBindViewHolder’da bind’ı kullanarak yapıyoruz. init’i ise eğer kullanıcı bir item’a

tıklarsa gideceği detail ekranındaki gösterilen verileri göndermeye ve detail ekranını açmak için kullanılıyor.
6-) fun bind’in içindeki fonskiyonlar kodun düzenli görülmesi için oluşturuldu. Kullanım amaçları şöyledir:
• fun makeFormat(textView: TextView, data:Double) -> Gelen json’da percent_change’ler çok fazla digit içeriyor ve tasarımda gelen pozitif verilerin başında ‘+’ işareti olması isteniyor. Buna uygun olarak örneğin -0.23138083 sayısı -0.23% olarak ve 0.48326019 sayısı da +0.48% olarak dönüştürülüyor. Bu formata önümüzdeki açıklamalarda Locale.English formatı diyeceğim.
• fun plusOrMinus(textView: TextView, data:Double, price:Double)-> Tasarımda Main ekranda görülen 24 saatlik yüzdelik değişimin yanında fiyat olarak da ne kadar değiştiğini hesaplıyoruz. Sonrasında da hesaplanan sayıyı Locale.English formatına çeviriyoruz.
• fun sixOrTwoDecimal(textView: TextView, data:Double) -> Eğer coin’nin fiyatı 1 doların altındaysa virgülden sonra 6 decimalin gösterilmesine izin veriyoruz. 1 doların üstündeyse virgülden sonra 2 decimal gösteriliyor.
• fun greenOrRed(textView: TextView, data:Double) -> Burada gelen yüzde eksiyse rengini kırmızı yapıyor. Pozitifse yeşil yapıyor.
• fun makeFormatForTwo(textView: TextView, data:Double) -> Locale.English formatına dönüştürüyor ve virgülden sonra 2 decimal sayıya izin veriyor.
• fun controlMaxSupply(textView: TextView, data:Double) -> Örneğin ethereum ve tether gibi coinler max supply’ı belli değil. Bu tarz coinleri için ‘It is not known’ yazısını gösteriyoruz.
• fun decideTextSize(textView: TextView, data:Double) -> Bazı coinlerin isimleri çok uzun olduğu için ana ekrandaki itemlarda coin ismi ve fiyat değişim yazısının üst üste gelme ihtimali doğuyor. Bu yüzden bu fonksiyonu kullanıyoruz. Örneğin ‘GLOBALTRUSTFUND TOKEN’ yazısının büyüklüğünü 12 sp’ye indiriyor.
7-) Main Activity’i yazmaya başladım. onCreate’in içinde recyclerview’imizi kurmak için setupRecyclerView() fonksiyonunu yazdım. Bütün verilerin bir anda gelmemesi, uygulamamızın kasmaması için page yapısını kullandım. Ekran scroll edildikçe verilerimiz yüklenecek. Bunun için addOnScrollListener’ımızı onCreate’in içine koydum. rankedBy.setOnItemClickListener içinde ise seçilen item’a göre sort değeri değiştiriliyor ve getCoinsWithRank fonksiyonu çağırılıyor. Defaultta ise getCoins fonksiyonu çağrılıyor. onRefresh fonksiyonumuz ise ekranın en üstünde ekranı aşağı doğru çekersek bize default sırayı geri getiriyor. Getirdikten sonra spinner'ımız açılıyor. Eğer istersek seçim yapıp değiştirebiliriz. fillRankMenu ise yapılan onRefresh sonrası spinner'ımızın içini doldurmaya yarıyor.
8-) En sonunda ise DetailActivity’i yazdım. onCreate’in içinde backButton’umuzun geriye gitmesi için gerekenler ve takeValues fonksiyonu yer alıyor. takeValues fonksiyonunda ise diğer ekrandan gönderilen veriler alınıyor ve view’lara veriliyor. greenOrRed fonksiyonu ise gelen verilerden negatif olanları kırmızı pozitif olanları ise yeşil rengi vermek için kullanılıyor. Detail

ekranında high ve low değerlerini görünmez yaptım çünkü yeni ücretsiz apilerde bu değerler gelmiyor.
