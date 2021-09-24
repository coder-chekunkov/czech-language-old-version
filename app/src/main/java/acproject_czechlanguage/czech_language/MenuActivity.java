package acproject_czechlanguage.czech_language;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.Locale;

public class MenuActivity extends Activity implements View.OnClickListener, RewardedVideoAdListener {

    //Переменная для кнопки "Начать игру":
    ImageButton button_information, button_shop, button_start;
    //Переменная для прогресса кол-ва правильно выполенных слов +
    // переменная для показа оставшихся игр на сегодня:
    private ProgressBar pbHorizontal_decided_questions, pbHorizontal_last_questions, pbHorizontal_decided_places ;
    //Переменная для кол-ва правильно выполенных слов +
    // переменная для кол-ва оставшихся игр на сегодня:
    private int progress_decided_questions = 0, progress_last_questions = 0;
    //Переменная для текста кол-ва правильно выполенных слов +
    // переменная для текста показа кол-ва оставшихся игр на сегодня:
    TextView text_decided_questions, text_last_questions;
    //Переменная для диалогового окна с информацией +
    // переменная для закарытия диалогового окна:
    Dialog alert_dialog_information;
    ImageView close_alert_dialog_information;
    //Переменная для диалогового окна с магазином +
    // переменная для закарытия диалогового окна:
    Dialog alert_dialog_shop;
    //Переменные для кнопок "награждение", "премиум версия", "помочь разработчику":
    ImageView close_alert_dialog_shop, button_shop_reward,
            button_shop_premium, button_shop_help;
    //Переменные для смены CardView:
    ViewFlipper vf; int vf_int = 1;
    //Переменная для диалогового окна с информацией о отсутвии игр на сегодня +
    // переменная для закарытия диалогового окна:
    Dialog alert_dialog_no_game_more;
    ImageView close_alert_dialog_no_game_more;
    //Переменная для диалогового окна со статистикой +
    // переменная для закарытия диалогового окна:
    Dialog alert_dialog_statistic;
    ImageView close_alert_dialog_statistic;
    //Переменные для регистрации кнопок ответов:
    ImageButton button_answer_yes, button_answer_no;
    //Переменная для количества игр оставшихся на сегодня:
    int games_in_day = 100; int max_decided_questions = 554;
    //Переменные для вопросов:
    TextView text_game_one, text_game_two;
    //Переменная для кнопки настроек:
    ImageButton button_settings;
    //Переменная для диалогового окна настроек +
    // переменная для закарытия настроек:
    Dialog alert_dialog_settings;
    ImageView close_alert_dialog_settings, button_settings_restart,
            button_settings_call, button_settings_review;
    //Кнопка для перехода в инстаграмм:
    ImageButton button_network;
    //Дополнительные кнопки:
    ImageButton smile_one, smile_two;
    ImageButton imageViewGame;
    //Переменная карточки:
    RelativeLayout relativeLayout_game;
    //Переменная Для "AdMob":
    private InterstitialAd interstitialAd;
    int isAd = 0;
    //Перменные для стаитистики всех сыгранных попыток:
    TextView text_all_answers; int all_answers;
    //Переменная для статистики оставшихся попыток:
    TextView statistic_last_questions;
    //Переменные для статистики всех правильных ответов:
    TextView statistic_good_answers; int good_answers;
    //Переменные для статистики всех не правильных ответов:
    TextView statistic_bas_answers; int bad_answers;
    //Переменная для Рекламы за награждение:
    private RewardedVideoAd mRewardedVideoAd;
    //Переменные для работы таймера:
    private static final long START_TIME_IN_MILLIS = 86400000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mEndTime;
    TextView text_shop_timer, text_no_game_more_timer, text_information_timer;
    boolean isTRG;
    boolean isAdshowed = false;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Регистрация рекламы:
        MobileAds.initialize(this, "ca-app-pub-2428921165694784");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        //Загрузка видео рекламы:
        loadRewardedVideoAd();

        //Регистрация карточки-игры:
        relativeLayout_game = (RelativeLayout) findViewById(R.id.relative_layout_game);
        relativeLayout_game.setOnClickListener(this);

        //Регистрация кнопки настроек:
        button_settings = (ImageButton) findViewById(R.id.button_settings);
        button_settings.setOnClickListener(this);

        //Регистрация + анимация кнопки "Начать игру":
        button_start = (ImageButton) findViewById(R.id.button_play_game);
        button_start.setOnClickListener(this);
        AnimationDrawable animationDrawable = (AnimationDrawable) button_start.getBackground();
        animationDrawable.setEnterFadeDuration(500);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();

        //Регистрация + анимация кнопки "Информация":
        button_information = (ImageButton) findViewById(R.id.button_information);
        button_information.setOnClickListener(this);
        AnimationDrawable animationDrawableInfo = (AnimationDrawable) button_information.getBackground();
        animationDrawableInfo.setEnterFadeDuration(100);
        animationDrawableInfo.setExitFadeDuration(500);
        animationDrawableInfo.start();

        //Регистрация анимации отделения объектов №1:
        View view_one = (View) findViewById(R.id.card_view_view_one);
        AnimationDrawable animationDrawableViewOne = (AnimationDrawable) view_one.getBackground();
        animationDrawableViewOne.setEnterFadeDuration(100);
        animationDrawableViewOne.setExitFadeDuration(500);
        animationDrawableViewOne.start();

        //Регистрация анимации отделения объектов №2:
        View view_two = (View) findViewById(R.id.card_view_view_two);
        AnimationDrawable animationDrawableViewTwo = (AnimationDrawable) view_two.getBackground();
        animationDrawableViewTwo.setEnterFadeDuration(100);
        animationDrawableViewTwo.setExitFadeDuration(500);
        animationDrawableViewTwo.start();

        //Регистрация анимации изображения:
        imageViewGame = (ImageButton) findViewById(R.id.image_button_card_view_start);
        imageViewGame.setOnClickListener(this);
        AnimationDrawable animationDrawableImageGame = (AnimationDrawable) imageViewGame.getBackground();
        animationDrawableImageGame.setEnterFadeDuration(100);
        animationDrawableImageGame.setExitFadeDuration(500);
        animationDrawableImageGame.start();

        //Регистрация + анимация кнопки "Магазин":
        button_shop = (ImageButton) findViewById(R.id.button_shop);
        button_shop.setOnClickListener(this);
        AnimationDrawable animationDrawableShop = (AnimationDrawable) button_shop.getBackground();
        animationDrawableShop.setEnterFadeDuration(100);
        animationDrawableShop.setExitFadeDuration(500);
        animationDrawableShop.start();

        //Вспоминаем кол-во правильно выполненых вопросов:
        SharedPreferences dq = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
        progress_decided_questions = dq.getInt("dq", 0);

        //Регистрация прогресса кол-ва правильно выполенных слов:
        pbHorizontal_decided_questions = (ProgressBar) findViewById(R.id.progressBar_decided_questions);
        pbHorizontal_decided_questions.setMax(max_decided_questions);
        setPbHorizontal_decided_questions(progress_decided_questions);

        //Регистрация текста кол-ва правильно выполенных слов:
        text_decided_questions = (TextView) findViewById(R.id.text_decided_questions);
        text_decided_questions.setText(progress_decided_questions + "/" + max_decided_questions);

        //Вспоминаем кол-во оставшихся вопросов:
        SharedPreferences lq = getSharedPreferences("last_questions", MenuActivity.MODE_PRIVATE);
        progress_last_questions = lq.getInt("lq", 0);

        SharedPreferences gid = getSharedPreferences("games_in_day", MenuActivity.MODE_PRIVATE);
        games_in_day = gid.getInt("gid", 100);

        //Регистрация прогресса кол-ва оставшихся игр на сегодня:
        pbHorizontal_last_questions = (ProgressBar) findViewById(R.id.progressBar_last_questions);
        pbHorizontal_last_questions.setMax(games_in_day);
        setProgress_last_questions(progress_last_questions);

        //Регистрация текста кол-ва оставшихся игр на сегодня:
        text_last_questions = (TextView) findViewById(R.id.text_last_questions);
        text_last_questions.setText(progress_last_questions + "/" + games_in_day);

        //Регистрация мазагина + регистрация информации + регистрация "нет игр больше" + регистрация настроек:
        alert_dialog_information = new Dialog(this);
        alert_dialog_shop = new Dialog(this);
        alert_dialog_no_game_more = new Dialog(this);
        alert_dialog_settings = new Dialog(this);
        alert_dialog_statistic = new Dialog(this);

        //Регистрация смены переменных CardView:
        vf = (ViewFlipper)findViewById(R.id.vf);

        //Регистрация анимации отделения объектов №1 на игровом CardView:
        View view_game_one = (View) findViewById(R.id.card_view_game_one);
        AnimationDrawable animationDrawableViewGameOne = (AnimationDrawable) view_game_one.getBackground();
        animationDrawableViewGameOne.setEnterFadeDuration(100);
        animationDrawableViewGameOne.setExitFadeDuration(500);
        animationDrawableViewGameOne.start();

        //Регистрация анимации изображения:
        ImageButton imageViewGameTwo = (ImageButton) findViewById(R.id.image_view_card_view_game_play);
        AnimationDrawable animationDrawableImageGameTwo = (AnimationDrawable) imageViewGameTwo.getBackground();
        animationDrawableImageGameTwo.setEnterFadeDuration(100);
        animationDrawableImageGameTwo.setExitFadeDuration(500);
        animationDrawableImageGameTwo.start();

        //Регистрация кнопок ответов:
        button_answer_yes = (ImageButton) findViewById(R.id.button_answer_yes);
        button_answer_no = (ImageButton) findViewById(R.id.button_answer_no);

        //Регистрация анимации изображения:
        ImageButton imageViewButtonAnswerYes = (ImageButton) findViewById(R.id.button_answer_yes);
        AnimationDrawable animationDrawableButtonAnswerYes = (AnimationDrawable) imageViewButtonAnswerYes.getBackground();
        animationDrawableButtonAnswerYes.setEnterFadeDuration(100);
        animationDrawableButtonAnswerYes.setExitFadeDuration(500);
        animationDrawableButtonAnswerYes.start();

        //Регистрация анимации изображения:
        ImageButton imageViewButtonAnswerNo = (ImageButton) findViewById(R.id.button_answer_no);
        AnimationDrawable animationDrawableButtonAnswerNo = (AnimationDrawable) imageViewButtonAnswerNo.getBackground();
        animationDrawableButtonAnswerNo.setEnterFadeDuration(100);
        animationDrawableButtonAnswerNo.setExitFadeDuration(500);
        animationDrawableButtonAnswerNo.start();

        //Регистрация текста воспросов:
        text_game_one = (TextView) findViewById(R.id.text_game_one);
        text_game_two = (TextView) findViewById(R.id.text_game_two);

        //Регистрация кнопки перехода в инстаграм:
        button_network = (ImageButton) findViewById(R.id.button_network);
        button_network.setOnClickListener(this);

        SharedPreferences sp = getSharedPreferences("checkbox", 0);
        isTRG = sp.getBoolean("isLogin", isTRG);

        if(isTRG == false) {
            pbHorizontal_last_questions.setMax(games_in_day);
            setProgress_last_questions(progress_last_questions);
            text_last_questions.setText(progress_last_questions + "/" + games_in_day);
        }

    }

    public void Game(){
        //Создаем Анимацию, для появления Текста и Картинок:
        final Animation anim = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.anim);
        final Animation anim_text_start = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.anim_text_start);
        final Animation anim_text_back = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.anim_text_back);
        final long mills = 1000L;
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        int wrong_word = 0; int is_word = 0 ;
        final int is_true;
        final char dm = (char) 34;
        final String[] czech_words  = new String[] {"Univerzita", "Tramvaj", "Ulice", "Most", "Les", "Voják", "Vesnice", "Řeka" ,"Hospodářství", "Dům", "Vlajka", "Katedrala", "Socha", "Na trhu", "Město", "Palác", "Noc", "Dobré ráno", "Dobry den", "Ahoj",
                "Prosim", "Promiňte", "Turista", "Moře", "Přítel", "Sníh", "Léto", "Zima", "Věčný", "Podzim", "Déšť", "Osoba", "Dovolená", "Střecha", "Milovat", "Mapa", "Hodiny", "Jídlo", "Kavárna", "Dítě", "Škola", "Zvíře", "Trdelnik", "Výkres", "Válka", "Jezero", "Pondělí", "Uterý", "Středa", "Čtvrtek", "Pátek", "Sobota", "Neděle", "Letiště", "Sladkost", "Hlavní Město", "Kino", "Přátelství", "Mlha", "Krása", "Kostel", "Hrůza", "Kolo", "Pták", "Hrad", "Člun", "Čísla", "Počítač", "Manžel", "Manželka", "Dítě", "Babička", "Jazyk", "Hudba", "Pero", "Učitel", "Autobus", "Postel", "Židle", "Stůl", "Maminka", "Vítěz", "Král", "Učebnice", "Tužka", "Student", "Cigareta", "Zelenina", "Ovoce", "Bobule", "Táto", "Ztroskotanec", "Píseň", "Zápisník", "Prostěradlo", "Trolejbus", "Alkohol", "Jablko", "Telefon", "Parfém", "Dědeček" , "Literatura", "Vůně", "Rodina", "Podvodník", "Pozor", "Začátečník", "Chytrý", "Rvačka", "Daň", "Mzda", "Vrtulník", "Chalupa", "Pitomec", "Mládenec", "Vedro", "Určitě", "Úroda", "Sklep", "Chápat",
                "Palivo", "Mráz", "Vor", "Potraviny", "Zmrzlina", "Okurka", "Polévka", "Кlobása", "Kaki", "Sedadlo", "Mýdlo", "Čerpadlo", "Zrcadlo", "Chodidlo", "Bydlo", "Strašidlo", "Ponožky", "Koruna česká", "Peníze", "Pivo", "Ano", "Ne", "Možná", "Nemluvím", "Rodiče", "Sestra", "Bratr", "Strýc", "Teta", "Pracovník", "Кočka", "Pes", "Divadlo", "Kavárna", "Hotel", "Zahrada", "Banka", "Zastávka", "Nemocnice", "Policie",
                "Pošta", "Nádraží", "Centrum", "Hora", "Kuchyně", "Balkón", "Koupelna", "Sprcha", "Toaleta", "Chodba", "Polštář", "Lednička", "Chléb", "Meloun", "Zákusek", "Rajče", "Káva", "Mléko", "Džus", "Hrozny", "Snídaně", "Oběd", "Večeře", "Vlak", "Rok", "Týden", "Hodina", "Minuta", "Ráno", "Večer", "Adresa", "Leden", "Únor", "Březen", "Duben", "Smět", "Červen", "Červenec", "Srpen", "Září",
                "Ríjen", "Listopad", "Prosinec", "Jméno ", "Příjmení ", "Narozeniny", "Hřeben", "Taška", "Přehrávač", "Obraz", "Míč", "Hračka ", "Obálka", "Letenka", "Jizdenka", "Sukně", "Rukavice", "Bunda", "Sako", "Tričko", "Kalhoty", "Levný", "Drahý", "Světlý", "Tmavý", "Černý", "Modrý", "Hnědý", "Zelený", "Červený", "Bílý", "Zlutý", "Být", "Děkuji", "Velikost", "Jezero", "Zámek", "Kostel", "Рřátelství", "Sladkost", "Obrázek", "Střecha", "Pozdrav", "Voják", "Ekonomika",
                "Standardní", "Tip", "Тyp", "Výjimka", "Zehlicí prkno", "Učitel", "Ridič", "Рracovník", "Inženýr", "Lékař", "Zdravotní sestra", "Рrodavač", "Učetní", "Malíř", "Země", "Rusko", "Českо", "Zvíře", "Kočka", "Рes", "Skola", "Divadlo", "Ulice", "Náměstí", "Místo", "Dům", "Сírkev", "Kavárna", "Hotel", "Zahrada", "Park", "Banka", "Zastávka", "Křižovatka", "Nemocnice", "Trh", "Policie", "Pošta", "Nádraží", "Centrum",
                "Hora", "Byt", "Kuchyně", "Balkón", "Koupelna", "Sprcha", "Toaleta", "Рodlaha", "Strop", "Patro", "Chodba", "Ložnice", "Obývací pokoj", "Dveře", "Okno", "Klíč", "Postel", "Polštář", "Stůl", "Zidle", "Křeslo", "Lednička", "Pohovka", "Zrcadlo", "Jídlo", "Chléb", "Máslo", "Sýr", "Klobása", "Olej", "Sůl", "Bobule", "Med", "Džem", "Houba", "Cibule", "Banán", "Mrkev", "Hruška", "Repa",
                "Meloun", "Vodní meloun", "Zákusek", "Koláč", "Dort", "Cokoláda", "Мaso", "Brambor", "Salát", "Rajče", "Zelí", "Kaše", "Polévka", "Sendvič", "Sodovka", "Voda", "Káva", "Сaj", "Mléko", "Džus", "Jablko", "Hrozny", "Pomeranč", "Ananas", "Meruňka", "Cukr", "Rýže", "Nudle", "hovězí", "Vepřové", "Кuře", "Rízek", "Citron", "Hrách", "Buchta", "Ryba", "Sladkost", "Zmrzlina", "Ořech", "Vejce",
                "Broskev", "Sálek", "Sklenice", "Talíř", "Lžíce", "Vidlička", "Nůž", "Podšálek", "Láhev", "Ubrousek", "Snídaně", "Oběd", "Večeře", "Letadlo", "Auto", "Tramvaj", "Autobus", "Vlak", "Kolo", "Rok", "Týden", "Hodina", "Minuta", "Jméno", "Příjmení", "Adresa", "Císlo", "Narozeniny", "Zenatý", "Vdaná", "Sachy", "Telefon", "Televizor", "Zehlička", "Mýdlo", "Rádio", "Taška", "Pohlednice", "Věc", "Kufr",
                "Dar", "Váza", "Kapesník", "Míč", "Balónek", "Hračka", "Učet", "Obálka", "Dopis", "Vstupenka", "Oděv", "Boty", "Kabát", "Saty", "Košile", "Sukně", "Rukavice", "Cepice", "Klobouk", "Bunda", "Sako", "Sátek", "Ponožka", "Svetr", "Tričko", "Kravata", "Kalhoty", "Kdo", "Кde", "Kam", "Odkud", "Jak", "Proč", "Kdy", "Jaký", "Který", "Já", "Ty", "On", "Ona",
                "Ono", "My", "Vy", "Oni", "Můj", "Tvůj", "Jeho", "Jí", "Naš", "Váš", "Jejich", "Svůj", "Mezi", "Podle", "Vedle", "Jeden", "Dva", "Tři", "Ctyři", "Pět", "Sest", "Sedm", "Osm", "Devět", "Deset", "Jedenáct", "Dvanáct", "Třináct", "Ctrnáct", "Patnáct", "Sestnáct", "Sedmnáct", "Osmnáct", "Devatenáct", "Dvacet", "Třicet", "Ctyřicet", "Padesát", "Sedesát", "Sedmdesát", "Osmdesát", "Devadesát", "Sto", "Tisíc", "Starý", "Mladý", "Nový", "Velký", "Malý", "Hladový", "Dobrý", "Spatný", "Dobře", "Spatně", "Brzy", "Později", "Minulý", "Příští", "Volný", "Teplo",
                "Сhladno", "Vysoký", "Nízký", "Dlouhý", "Krátký", "Snadný", "Obtížný", "Drahý", "Levný", "Vlevo", "Vpravo", "Správný", "Rychle", "Pomalu", "Měkký", "Tvrdý", "Pozorný", "Smutný", "Sťastný", "Hlavní",
                "Strakhova", "Slovníček frází", "Reklamní", "Anglie", "Angličan", "Francie", "Francouz", "Německo", "Polsko", "Pól",
                "Belgie", "Belgický", "Holandsko", "Holanďan", "Španělsko", "Hispánský", "Portugalsko", "Portugalština", "Rakousko", "Rakouský",
                "Švýcarsko", "Svýcarský", "Itálie", "Italština", "Bělorusko", "Bělorus", "Ukrajina", "Ukrajinština", "Kazachstán", "Kazašský"


        };

        final String[] russian_words  = new String[] {"Университет", "Трамвай", "Улица", "Мост", "Лес", "Солдат", "Деревня", "Река" ,"Экономика", "Дом", "Флаг", "Собор", "Статуя", "Рынок", "Город", "Дворец", "Ночь", "Доброе утро", "Добрый день", "Привет",
                "Пожалуйста", "Извините", "Туристы", "Море", "Друг", "Снег", "Лето", "Зима", "Весна", "Осень", "Дождь", "Человек", "Праздник", "Крыша", "Любовь", "Карта", "Часы", "Еда", "Кафе", "Ребёнок", "Школа", "Животные", "Трдельник", "Рисунок", "Война", "Озеро", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье", "Аэропорт", "Сладость", "Столица", "Кинотеатр", "Дружба", "Туман", "Красота", "Церковь", "Ужас", "Велосипед", "Птица", "Замок", "Лодка", "Цифры", "Компьютер", "Муж", "Жена",
                "Ребенок", "Бабушка", "Язык", "Музыка", "Ручка", "Учитель", "Автобус", "Кровать", "Стул", "Стол", "Мама", "Победитель", "Король", "Учебник", "Карандаш", "Ученик", "Сигарета", "Овощи", "Фрукты", "Ягоды", "Папа", "Проигравший", "Песня", "Тетрадь", "Лист", "Троллейбус", "Алкоголь", "Яблоко", "Телефон", "Духи", "Дедушка", "Литература", "Запах", "Семья", "Мошенник", "Внимание", "Водитель", "Умный", "Драка", "Налог", "Зарплата", "Вертолет", "Дача", "Глупец", "Холостяк", "Жара", "Точно", "Урожай", "Подвал", "Понимать",
                "Топливо", "Мороз", "Плот", "Продуктовые товары", "Мороженое", "Огурец", "Суп", "Колбаса", "Хурма", "Кресло", "Мыло", "Насос", "Зеркало", "Ходило", "Жизнь", "Привидение", "Носки", "Чешская крона", "Деньги", "Пиво", "Да", "Нет", "Возможно", "Не понимать", "Родители", "Сестра", "Брат", "Дядя", "Тетя", "Рабочий", "Кошка", "Пес", "Театр", "Кафе", "Отель", "Сад", "Банк", "Остановка", "Больница", "Полиция",
                "Почта", "Вокзал", "Центр", "Гора", "Кухня", "Балкон", "Ванная", "Душ", "Туалет", "Коридор", "Подушка", "Холодильник", "Хлеб", "Арбуз", "Пироженое", "Помидор", "Кофе", "Молоко", "Сок", "Виноград", "Завтрак", "Обед", "Ужин", "Поезд", "Год", "Неделя", "Час", "Минута", "Утро", "Вечер", "Адрес", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь",
                "Октябрь", "Ноябрь", "Декабрь", "Имя", "Фамилия", "День рождения", "Расческа", "Сумка", "Плейр", "Картина", "Мяч", "Игрушка", "Конверт", "Билет", "Проездной", "Рубашка", "Перчатка", "Куртка", "Пиджак", "Рубашка", "Брюки", "Дешёвый", "Дорогой", "Светлый", "Темный", "Черный", "Голубой", "Коричневый", "Зеленый", "Красный", "Белый", "Желтый", "Быть", "Спасибо", "Размер",
                "Озеро", "Замок", "Церковь", "Дружба", "Сладость", "Рисунок", "Крыша", "Салют", "Солдат", "Экономика", "Стандартный", "Рекомендация", "Тип", "Исключение", "Гладильная доска", "Учитель", "Водитель", "Рабочий", "Инженер", "Врач", "Медсестра", "Продавец", "Бухгалтер", "Художник", "Страна", "Россия", "Чехия", "Живтоное", "Кошка", "Собака", "Школа", "Театр", "Улица", "Площадь", "Место", "Дом", "Церковь", "Кафе", "Отель", "Сад", "Парк", "Банк", "Остановка", "Перекресток", "Больница", "Рынок", "Полиция", "Почта", "Вокзал", "Центр",
                "Гора", "Квартира", "Кухня", "Балкон", "Ванная", "Душ", "Туалет", "Пол в квартире", "Потолок", "Этаж", "Коридор", "Спальня", "Зал", "Дверь", "Окно", "Ключ", "Кровать", "Подушка", "Стол", "Стул", "Кресло", "Холодильник", "Диван", "Зеркало", "Еда", "Хлеб", "Масло", "Сыр", "Колбаса", "Растительное масло", "Соль", "Ягода", "Мёд", "Варенье", "Гриб", "Лук", "Банан", "Морковь", "Груша", "Свекла",
                "Дыня", "Арбуз", "Пирожное", "Пирог", "Торт", "Шоколад", "Мясо", "Картофель", "Салат", "Помидор", "Капуста", "Кaše", "Суп", "Бутерброд", "Газировка", "Вода", "Кофе", "Чай", "Молоко", "Сок", "Яблоко", "Виноград", "Апельсин", "Ананас", "Абрикос", "Сахар", "Рис", "Лапша", "Говядина", "Свинина", "Курица", "Котлета", "Лимон", "Горох", "Булочка", "Рыба", "Конфета", "Мороженое", "Орех", "Яйцо",
                "Персик", "Чашка", "Стакан", "Тарелка", "Ложка", "Вилка", "Нож", "Блюдце", "Бутылка", "Салфетка", "Завтрак", "Обед", "Ужин", "Самолет", "Автомобиль", "Трамвай", "Автобус", "Поезд", "Велосипед", "Год", "Неделя", "Час", "Минута", "Имя", "Фамилия", "Адрес", "Число", "День рождения", "Женатый мужчина", "Замужняя женщина", "Шахматы", "Телефон", "Телевизор", "Утюг", "Мыло", "Радио", "Сумка", "Открытка", "Вещь", "Чемодан",
                "Подарок", "Ваза", "Носовой Платок", "Мяч", "Воздушный шар", "Игрушка", "Счет", "Конверт", "Письмо", "Билет в кино", "Одежда", "Обувь", "Пальто", "Платье", "Рубашка", "Юбка", "Перчатка", "Шапка", "Шляпа", "Куртка", "Пиджак", "Шарф", "Носок", "Свитер", "Футболка", "Галстук", "Брюки", "Кто", "Где", "Куда", "Откуда", "Как", "Почему", "Когда", "Какой", "Который", "Я", "Ты", "Он", "Она",
                "Оно", "Мы", "Вы", "Они", "Мой", "Твой", "Его", "Её", "Наш", "Ваш", "Их", "Свой", "Между", "Согласно", "Возле", "Один", "Два", "Три", "Четыре", "Пять", "Шесть", "Семь", "Восемь", "Девять", "Десять", "Одиннадцать", "Двенадцать", "Тринадцать", "Четырнадцать", "Пятнадцать", "Шестнадцать", "Семнадцать", "Восемнадцать", "Девятнадцать", "Двадцать", "Тридцать", "Сорок", "Пятьдесят", "Шестьдесят", "Семьдесят",
                "Восемьдесят", "Девяносто", "Сто", "Тысяча", "Старый", "Молодой", "Новый", "Большой", "Маленький", "Голодный", "Хороший", "Плохой", "Хорошо", "Плохо", "Рано", "Поздно", "Прошлый", "Следующий", "Свободный", "Тепло", "Холодно", "Высокий", "Низкий", "Длинный", "Короткий", "Сладкий", "Тяжелый", "Дорогой", "Дешевый", "Влево", "Вправо", "Правильный", "Быстрый", "Медленно", "Мягкий", "Твердый", "Внимательный", "Печальный", "Счастливый", "Главный",
                "Страхова", "Разговорник", "Реклама", "Англия", "Англичанин", "Франция", "Француз", "Германия", "Польша", "Поляк",
                "Бельгия", "Бельгиец", "Нидерланды", "Нидерландец", "Испания", "Испанец", "Португалия", "Португалец", "Австрия", "Австриец",
                "Швейцария", "Швейцарец", "Италия", "Итальянец", "Белоруссия", "Белорусс", "Украина", "Украинец", "Казахстан", "Казах"



        };

        int kol_true = 554;
        int kol_wrong = kol_true -1;
        is_true = (int) (Math.random()*(1+1)) + 1;
        is_word = (int) (Math.random() * ++kol_true);

        if(is_true % 2 == 0){
            text_game_one.startAnimation(anim_text_start);
            text_game_one.setText(dm + czech_words[is_word] + dm);
            text_game_two.startAnimation(anim_text_start);
            text_game_two.setText(dm + russian_words[is_word] + dm);
        }

        if(is_true % 2 == 1){
            wrong_word = is_true;
            while (is_true == wrong_word){
                wrong_word = (int) (Math.random() * ++kol_wrong);
            }
            text_game_one.startAnimation(anim_text_start);
            text_game_one.setText(dm + czech_words[is_word] + dm);
            text_game_two.startAnimation(anim_text_start);
            text_game_two.setText(dm + russian_words[wrong_word] + dm);

        }

        final int finalIs_word = is_word;
        final int finalIs_word1 = is_word;
        smile_one = (ImageButton) findViewById(R.id.smile_one);
        smile_one.setVisibility(View.INVISIBLE);
        smile_two = (ImageButton) findViewById(R.id.smile_two);
        smile_two.setVisibility(View.INVISIBLE);
        button_answer_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isAd == 10){
                    AdRequest adRequest = new AdRequest.Builder().build();
                    //Реклама:
                    MobileAds.initialize(MenuActivity.this, "ca-app-pub-2428921165694784");
                    interstitialAd = new InterstitialAd(MenuActivity.this);
                    interstitialAd.setAdUnitId("ca-app-pub-2428921165694784/9054588743");
                    interstitialAd.loadAd(adRequest);
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            if(interstitialAd.isLoaded()){
                                interstitialAd.show();
                            }else{
                                Log.d("TAG", "Error");
                            }
                        }
                    });
                    isAd = 0;
                }
                isAd++;

                //Игрок ответиил верно:
                if(is_true % 2 == 0){
                    //Увеличиваем и запоминаем все сыгранные попытки:
                    all_answers++;
                    SharedPreferences aa = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
                    SharedPreferences.Editor editor_aa = aa.edit();
                    editor_aa.putInt("aa", all_answers);
                    editor_aa.commit();

                    //Увеличиваем и запомниаем все правильные ответы:
                    good_answers++;
                    SharedPreferences ga = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
                    SharedPreferences.Editor editor_ga = ga.edit();
                    editor_ga.putInt("ga", good_answers);
                    editor_ga.commit();

                    smile_one.setImageResource(R.drawable.good_smile);
                    smile_two.setImageResource(R.drawable.good_smile);
                    smile_one.startAnimation(anim);
                    smile_two.startAnimation(anim);
                    text_game_one.startAnimation(anim_text_back);
                    text_game_two.startAnimation(anim_text_back);

                    Toast toast = Toast.makeText(getApplicationContext(), "Верно! " + dm + czech_words[finalIs_word]+ dm + " = " + dm + russian_words[finalIs_word1] + dm, Toast.LENGTH_SHORT);
                    toast.show();
                    if(progress_decided_questions != max_decided_questions){
                        progress_decided_questions++;
                    }
                        //Запоминаем кол-во правильно выполненых вопросов:
                        SharedPreferences dq = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = dq.edit();
                        editor.putInt("dq", progress_decided_questions);
                        editor.commit();
                }
                //Игрок ответиил неверно:
                if(is_true % 2 == 1){
                    //Увеличиваем и запоминаем все сыгранные попытки:
                    all_answers++;
                    SharedPreferences aa = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
                    SharedPreferences.Editor editor_aa = aa.edit();
                    editor_aa.putInt("aa", all_answers);
                    editor_aa.commit();

                    //Увелисиваем и запомниаем все не правильные ответы:
                    bad_answers++;
                    SharedPreferences ba = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
                    SharedPreferences.Editor editor_ba = ba.edit();
                    editor_ba.putInt("ba", bad_answers);
                    editor_ba.commit();

                    smile_one.setImageResource(R.drawable.wrong_smile);
                    smile_two.setImageResource(R.drawable.wrong_smile);
                    smile_one.startAnimation(anim);
                    smile_two.startAnimation(anim);
                    text_game_one.startAnimation(anim_text_back);
                    text_game_two.startAnimation(anim_text_back);
                    vibrator.vibrate(mills);

                    Toast toast = Toast.makeText(getApplicationContext(), "Неверно! " + dm + czech_words[finalIs_word]+ dm + " = " + dm + russian_words[finalIs_word1] + dm, Toast.LENGTH_SHORT);
                    toast.show();
                    if(progress_decided_questions != 0){
                        progress_decided_questions--;
                    }
                        //Запоминаем кол-во правильно выполненых вопросов:
                        SharedPreferences dq = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = dq.edit();
                        editor.putInt("dq", progress_decided_questions);
                        editor.commit();
                }
                if(progress_last_questions < games_in_day) {
                    //Изменение прогресса с количетсвом оставшихся игра на сегодня:
                    progress_last_questions = progress_last_questions + 1;
                        //Запоминаем кол-во оставшихся вопросов:
                        SharedPreferences lq = getSharedPreferences("last_questions", MenuActivity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = lq.edit();
                        editor.putInt("lq", progress_last_questions);
                        editor.commit();


                    text_last_questions.setText(progress_last_questions + "/" + games_in_day);
                    setProgress_last_questions(progress_last_questions);
                        //Вспоминаем кол-во правильно выполненых вопросов:
                        SharedPreferences dq = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
                        progress_decided_questions = dq.getInt("dq", -1);
                    //Изменение прогресса решенных вопросов:
                    text_decided_questions.setText(progress_decided_questions + "/" + max_decided_questions);
                    setPbHorizontal_decided_questions(progress_decided_questions);
                    Game();
                }
                if(progress_last_questions >= games_in_day) {
                    showAlert_Dialog_No_Game_More();
                    vf.setDisplayedChild(vf_int);
                    button_start.setImageResource(R.drawable.icon_button_start);
                    vf_int = 1;
                }

            }
        });

        button_answer_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isAd == 10){
                    AdRequest adRequest = new AdRequest.Builder().build();
                    //Реклама:
                    MobileAds.initialize(MenuActivity.this, "ca-app-pub-2428921165694784");
                    interstitialAd = new InterstitialAd(MenuActivity.this);
                    interstitialAd.setAdUnitId("ca-app-pub-2428921165694784/9322855163");
                    interstitialAd.loadAd(adRequest);
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            if(interstitialAd.isLoaded()){
                                interstitialAd.show();
                            }else{
                                Log.d("TAG", "Error");
                            }
                        }
                    });
                    isAd = 0;
                }
                isAd++;
                //Игрок ответиил неверно:
                if(is_true % 2 == 0){
                    //Увеличиваем и запоминаем все сыгранные попытки:
                    all_answers++;
                    SharedPreferences aa = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
                    SharedPreferences.Editor editor_aa = aa.edit();
                    editor_aa.putInt("aa", all_answers);
                    editor_aa.commit();

                    //Увелисиваем и запомниаем все не правильные ответы:
                    bad_answers++;
                    SharedPreferences ba = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
                    SharedPreferences.Editor editor_ba = ba.edit();
                    editor_ba.putInt("ba", bad_answers);
                    editor_ba.commit();

                    smile_one.setImageResource(R.drawable.wrong_smile);
                    smile_two.setImageResource(R.drawable.wrong_smile);
                    smile_one.startAnimation(anim);
                    smile_two.startAnimation(anim);
                    text_game_one.startAnimation(anim_text_back);
                    text_game_two.startAnimation(anim_text_back);
                    vibrator.vibrate(mills);

                    Toast toast = Toast.makeText(getApplicationContext(), "Неверно! " + dm + czech_words[finalIs_word]+ dm + " = " + dm + russian_words[finalIs_word1] + dm, Toast.LENGTH_SHORT);
                    toast.show();
                    if(progress_decided_questions != 0){
                        progress_decided_questions--;
                    }
                        //Запоминаем кол-во правильно выполненых вопросов:
                        SharedPreferences dq = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = dq.edit();
                        editor.putInt("dq", progress_decided_questions);
                        editor.commit();
                }
                //Игрок ответиил верно:
                if(is_true % 2 == 1){
                    //Увеличиваем и запоминаем все сыгранные попытки:
                    all_answers++;
                    SharedPreferences aa = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
                    SharedPreferences.Editor editor_aa = aa.edit();
                    editor_aa.putInt("aa", all_answers);
                    editor_aa.commit();

                    //Увелисиваем и запомниаем все правильные ответы:
                    good_answers++;
                    SharedPreferences ga = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
                    SharedPreferences.Editor editor_ga = ga.edit();
                    editor_ga.putInt("ga", good_answers);
                    editor_ga.commit();

                    smile_one.setImageResource(R.drawable.good_smile);
                    smile_two.setImageResource(R.drawable.good_smile);
                    smile_one.startAnimation(anim);
                    smile_two.startAnimation(anim);
                    text_game_one.startAnimation(anim_text_back);
                    text_game_two.startAnimation(anim_text_back);

                    Toast toast = Toast.makeText(getApplicationContext(), "Верно! " + dm + czech_words[finalIs_word]+ dm + " = " + dm + russian_words[finalIs_word1] + dm, Toast.LENGTH_SHORT);
                    toast.show();
                    if(progress_decided_questions != max_decided_questions){
                        progress_decided_questions++;
                    }
                        //Запоминаем кол-во правильно выполненых вопросов:
                        SharedPreferences dq = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = dq.edit();
                        editor.putInt("dq", progress_decided_questions);
                        editor.commit();
                }
                if(progress_last_questions < games_in_day) {
                    //Изменение прогресса с количетсвом оставшихся игра на сегодня:
                    progress_last_questions = progress_last_questions + 1;
                        //Запоминаем кол-во оставшихся вопросов:
                        SharedPreferences lq = getSharedPreferences("last_questions", MenuActivity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = lq.edit();
                        editor.putInt("lq", progress_last_questions);
                        editor.commit();
                    text_last_questions.setText(progress_last_questions + "/" + games_in_day);
                    setProgress_last_questions(progress_last_questions);
                        //Вспоминаем кол-во правильно выполненых вопросов:
                        SharedPreferences dq = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
                        progress_decided_questions = dq.getInt("dq", -1);
                    //Изменение прогресса решенных вопросов:
                    text_decided_questions.setText(progress_decided_questions + "/" + max_decided_questions);
                    setPbHorizontal_decided_questions(progress_decided_questions);
                        Game();
                }
                if(progress_last_questions >= games_in_day){
                    showAlert_Dialog_No_Game_More();
                    vf.setDisplayedChild(vf_int);
                    button_start.setImageResource(R.drawable.icon_button_start);
                    vf_int = 1;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.relative_layout_game:
                startTimer();
                if(progress_last_questions < games_in_day) {
                    vf_int = 1;
                    //Смена CardView(Меню) на CardView(Игра):
                    vf.setDisplayedChild(vf_int);
                    if (vf_int == 1) {
                        vf_int = 2;
                        button_start.setImageResource(R.drawable.icon_button_stop);
                        Game();
                    }
                }
                if(progress_last_questions >= games_in_day) {
                    showAlert_Dialog_No_Game_More();
                }
                break;
            case R.id.button_play_game:
                    vf_int = 2;
                    vf.setDisplayedChild(vf_int);
                    button_start.setImageResource(R.drawable.icon_button_start);
                break;
            case R.id.image_button_card_view_find_place:
                Toast toast = Toast.makeText(getApplicationContext(), "Скоро!", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case R.id.button_information:
                showAlert_Dialog_Information();
                break;
            case R.id.button_shop:
                showAlert_Dialog_Shop();
                break;
            case R.id.button_settings:
                showAlert_Dialog_Settings();
                break;
            case R.id.button_network:
                showAlert_Dialog_Statistic();
                break;
        }
    }

    private void setPbHorizontal_decided_questions(int progress) {
        pbHorizontal_decided_questions.setProgress(progress);
    }
    private void setProgress_last_questions(int progress) {
        pbHorizontal_last_questions.setProgress(progress);
    }

    //Метод для работы с диалоговым окном статистики:
    public void showAlert_Dialog_Statistic(){
        //Вспоминаем кол-во всех сыгранных игр:
        SharedPreferences aa = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
        all_answers = aa.getInt("aa", 0);
        //Вспоминаем кол-во всех правильных ответов:
        SharedPreferences ga = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
        good_answers = ga.getInt("ga", 0);
        //Вспоминаем кол-во всех неправильных ответов:
        SharedPreferences ba = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
        bad_answers = ba.getInt("ba", 0);
        alert_dialog_statistic.setContentView(R.layout.custom_alert_dialog_statistic);
        text_all_answers = (TextView) alert_dialog_statistic.findViewById(R.id.text_count_all_answers);
        if(all_answers > 999){
            text_all_answers.setText("999+");
        }else{
            text_all_answers.setText(Integer.toString(all_answers));
        }
        statistic_last_questions = (TextView) alert_dialog_statistic.findViewById(R.id.count_last_questions);
        if(games_in_day - progress_last_questions > 999){
            statistic_last_questions.setText("999+");
        }else{
            statistic_last_questions.setText(Integer.toString(games_in_day - progress_last_questions));
        }
        statistic_good_answers = (TextView) alert_dialog_statistic.findViewById(R.id.count_good_answers);
        if(good_answers > 999){
            statistic_good_answers.setText("999+");
        }else{
            statistic_good_answers.setText(Integer.toString(good_answers));
        }
        statistic_bas_answers = (TextView) alert_dialog_statistic.findViewById(R.id.count_bad_answers);
        if(bad_answers > 999){
            statistic_bas_answers.setText("999+");
        }else{
            statistic_bas_answers.setText(Integer.toString(bad_answers));
        }
       close_alert_dialog_statistic = (ImageView) alert_dialog_statistic.findViewById(R.id.close_alert_dialog_statistic);
       close_alert_dialog_statistic.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               alert_dialog_statistic.dismiss();
           }
       });
        alert_dialog_statistic.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert_dialog_statistic.show();
    }

    //Метод для работы с диалоговым окном о отсуствии игр на сегодня:
    public void showAlert_Dialog_No_Game_More(){
        alert_dialog_no_game_more.setContentView(R.layout.custom_view_no_game_more);
        close_alert_dialog_no_game_more = (ImageView) alert_dialog_no_game_more.findViewById(R.id.close_alert_dialog_no_game_more);
        close_alert_dialog_no_game_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_dialog_no_game_more.dismiss();
            }
        });
        text_no_game_more_timer = alert_dialog_no_game_more.findViewById(R.id.no_game_more_text_timer);
        int hours = (int) (mTimeLeftInMillis / 3600000);
        int minutes = (int) mTimeLeftInMillis / 60000 % 60;
        int seconds = (int) mTimeLeftInMillis / 1000 % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        text_no_game_more_timer.setText(timeLeftFormatted);
        alert_dialog_no_game_more.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert_dialog_no_game_more.show();
    }

    //Метод для работы с диалоговым окном информации:
    public void showAlert_Dialog_Information(){
        alert_dialog_information.setContentView(R.layout.custom_alert_dialog_information);
        close_alert_dialog_information = (ImageView) alert_dialog_information.findViewById(R.id.close_alert_dialog_information);
        close_alert_dialog_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_dialog_information.dismiss();
            }
        });
        text_information_timer = alert_dialog_information.findViewById(R.id.information_text_timer);
        int hours = (int) (mTimeLeftInMillis / 3600000);
        int minutes = (int) mTimeLeftInMillis / 60000 % 60;
        int seconds = (int) mTimeLeftInMillis / 1000 % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        text_information_timer.setText(timeLeftFormatted);
        alert_dialog_information.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert_dialog_information.show();
    }

    //Метод для работы с диалоговым окном магазина:
    public void showAlert_Dialog_Shop(){
        alert_dialog_shop.setContentView(R.layout.custom_alert_dialog_shop);
        close_alert_dialog_shop = (ImageView) alert_dialog_shop.findViewById(R.id.close_alert_dialog_shop);
        close_alert_dialog_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_dialog_shop.dismiss();
            }
        });
        button_shop_reward = (ImageView) alert_dialog_shop.findViewById(R.id.button_shop_reward);
        button_shop_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();

                    loadRewardedVideoAd();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Произошла ошибка! Проверьте подключение к интернету или попробуйте снова!", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        text_shop_timer = alert_dialog_shop.findViewById(R.id.shop_text_timer);
        int hours = (int) (mTimeLeftInMillis / 3600000);
        int minutes = (int) mTimeLeftInMillis / 60000 % 60;
        int seconds = (int) mTimeLeftInMillis / 1000 % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        text_shop_timer.setText(timeLeftFormatted);

        alert_dialog_shop.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert_dialog_shop.show();
    }

    public void showAlert_Dialog_Settings(){
        alert_dialog_settings.setContentView(R.layout.card_view_alert_dialog_settings);
        close_alert_dialog_settings = (ImageView) alert_dialog_settings.findViewById(R.id.close_alert_dialog_settings);
        close_alert_dialog_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_dialog_settings.dismiss();
            }
        });

        button_settings_restart = (ImageView) alert_dialog_settings.findViewById(R.id.button_settings_restart);
        button_settings_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "Ты обновил кол-во решенных вопросов!", Toast.LENGTH_SHORT);
                toast.show();
                alert_dialog_settings.dismiss();
                progress_decided_questions = 0;
                all_answers = 0;
                good_answers = 0;
                bad_answers = 0;
                //Вспоминаем кол-во всех сыгранных игр:
                SharedPreferences aa = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
                all_answers = aa.getInt("aa", 0);
                //Вспоминаем кол-во всех правильных ответов:
                SharedPreferences ga = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
                good_answers = ga.getInt("ga", 0);
                //Вспоминаем кол-во всех неправильных ответов:
                SharedPreferences ba = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
                bad_answers = ba.getInt("ba", 0);
                //Запоминаем кол-во правильно выполненых вопросов:
                SharedPreferences dq = getSharedPreferences("decided_questions", MenuActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = dq.edit();
                editor.putInt("dq", progress_decided_questions);
                editor.commit();
                pbHorizontal_decided_questions.setMax(max_decided_questions);
                setPbHorizontal_decided_questions(progress_decided_questions);
                text_decided_questions.setText(progress_decided_questions + "/" + max_decided_questions);
            }
        });
        button_settings_review = (ImageView) alert_dialog_settings.findViewById(R.id.button_settings_review);
        button_settings_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserReview = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=acproject_czechlanguage.czech_language"));
                startActivity(browserReview);
                alert_dialog_settings.dismiss();
            }
        });
        button_settings_call = (ImageView) alert_dialog_settings.findViewById(R.id.button_settings_call);
        button_settings_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final char dm = (char) 34;
                Intent intent_call_us = new Intent(Intent.ACTION_SEND);
                intent_call_us.putExtra(Intent.EXTRA_EMAIL, "czech.language@yandex.ru");
                intent_call_us.putExtra(Intent.EXTRA_TEXT, "Здравствуйте, команада разработчиков приложения " + dm + "Чешский Язык" + dm + ",");
                intent_call_us.setType("message/rfc822");
                startActivity(Intent.createChooser(intent_call_us, "Выберите Приложение:"));
            }
        });
        alert_dialog_settings.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert_dialog_settings.show();
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-2428921165694784/8125215102", new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        isAdshowed = true;
        games_in_day = games_in_day + 10;
        Toast toast = Toast.makeText(getApplicationContext(), "Вы получили 10 дополнительных игр!", Toast.LENGTH_SHORT);
        toast.show();
        //Запоминаем кол-во оставшихся игр на сегодня:
        SharedPreferences gid = getSharedPreferences("games_in_day", MenuActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = gid.edit();
        editor.putInt("gid", games_in_day);
        editor.commit();
        alert_dialog_shop.dismiss();
        pbHorizontal_last_questions.setMax(games_in_day);
        text_last_questions.setText(progress_last_questions + "/" + games_in_day);
        loadRewardedVideoAd();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    public void updateCountDownText(){
        int hours = (int) (mTimeLeftInMillis / 3600000);
        int minutes = (int) mTimeLeftInMillis / 60000 % 60;
        int seconds = (int) mTimeLeftInMillis / 1000 % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

        alert_dialog_shop.setContentView(R.layout.custom_alert_dialog_shop);
        close_alert_dialog_shop = (ImageView) alert_dialog_shop.findViewById(R.id.close_alert_dialog_shop);
        close_alert_dialog_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_dialog_shop.dismiss();
            }
        });
        button_shop_reward = (ImageView) alert_dialog_shop.findViewById(R.id.button_shop_reward);
        button_shop_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                    loadRewardedVideoAd();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Произошла ошибка! Проверьте подключение к интернету или попробуйте снова!", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
        text_shop_timer = alert_dialog_shop.findViewById(R.id.shop_text_timer);
        text_shop_timer.setText(timeLeftFormatted);

        alert_dialog_information.setContentView(R.layout.custom_alert_dialog_information);
        close_alert_dialog_information = (ImageView) alert_dialog_information.findViewById(R.id.close_alert_dialog_information);
        close_alert_dialog_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_dialog_information.dismiss();
            }
        });
        text_information_timer = alert_dialog_information.findViewById(R.id.information_text_timer);
        text_information_timer.setText(timeLeftFormatted);

        alert_dialog_no_game_more.setContentView(R.layout.custom_view_no_game_more);
        close_alert_dialog_no_game_more = (ImageView) alert_dialog_no_game_more.findViewById(R.id.close_alert_dialog_no_game_more);
        close_alert_dialog_no_game_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_dialog_no_game_more.dismiss();
            }
        });
        text_no_game_more_timer = alert_dialog_no_game_more.findViewById(R.id.no_game_more_text_timer);
        text_no_game_more_timer.setText(timeLeftFormatted);

    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                mTimerRunning = false;
                isTRG = false;

                SharedPreferences sp =getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor et = sp.edit();
                et.putBoolean("isLogin", isTRG);
                et.commit();



                games_in_day = 100; progress_last_questions = 0;
                SharedPreferences gid = getSharedPreferences("games_in_day", MenuActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = gid.edit();
                editor.putInt("gid", games_in_day);
                editor.commit();
                SharedPreferences lq = getSharedPreferences("last_questions", MenuActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = lq.edit();
                editor2.putInt("lq", progress_last_questions);
                editor2.commit();
                pbHorizontal_last_questions.setMax(games_in_day);
                setProgress_last_questions(progress_last_questions);
                text_last_questions.setText(progress_last_questions + "/" + games_in_day);
                mTimeLeftInMillis = 86400000;
                startTimer();
            }
        }.start();
        mTimerRunning = true;
    }
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.apply();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;

            } else {
                startTimer();
            }
        }
    }





}
