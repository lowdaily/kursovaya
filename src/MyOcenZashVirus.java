import java.awt.*;
import javax.swing.*; // Более совершенная библиотека компонентов
import java.io.*; // Пакет классов ввода выводы

class VredProg {
    static int N = 9; // Число признаков
    int Priz[]; // Ссылка на массив значений признаков
    static String Name[]; // Имена признаков
    static String Value[]; // Значения признаков
    static String Value2[]; // Значения признаков
    static int Zn_Pr = 5; // Число возможных значений признаков

    static {
        Name = new String[N];
        Value = new String[6];
        Value2 = new String[6];
        Name[0] = "Работа в локальной сети";
        Name[1] = "Работа в Интернет";
        Name[2] = "Активная работа с рекламными и неизвестными сайтами";
        Name[3] = "Использование сайтов, требующих регистрации";
        Name[4] = "Использование \"пиратского\" ПО";
        Name[5] = "Использование антивирусного ПО и его обновление";
        Name[6] = "Применение организационных мер защиты";
        Name[7] = "Работа с конфиденциальной информацией";
        Name[8] = "Число пользователей";
        Value[0] = "Очень низкое";
        Value[1] = "Низкое";
        Value[2] = "Среднее";
        Value[3] = "Высокое";
        Value[4] = "Очень высокое";
        Value[5] = "Безразлично";
        Value2[0] = "1";
        Value2[1] = "2-4";
        Value2[2] = "5-8";
        Value2[3] = "9-15";
        Value2[4] = ">15";
        Value2[5] = "Безразлично";

    }

    String NameProg; // Имя вредной программы

    public VredProg(int Pr[], String NamePr) // Конструктор, задает значения признаков
    {
        NameProg = NamePr;
        Priz = new int[N];
        for (int i = 0; i < N; i++)
            Priz[i] = Pr[i];
    }

    // Расчет расстояния между объектами по Евклидовой метрике
    public double getRast(VredProg Ob2) {
        double Rast = 0, R;
        int n = 0; // Число реально используемых признаков
        for (int i = 0; i < N; i++) // Сумма по признакам
            if (Priz[i] < 5) { // Признак используется (значение не безразлично)
                Rast += Math.pow((double) Priz[i] / (Zn_Pr - 1) - (double) Ob2.Priz[i] / (Zn_Pr - 1), 2);
                n++;
            }
        R = Math.sqrt(Rast);
        if (n != N) R = R * Math.sqrt(N) / Math.sqrt(n); // Нормировка расстояния
        return R;
    }

    // Пересчет расстояния в меру близости
    public double getMera(VredProg Ob2) {
        double maxM = Math.sqrt(N); // Теоретически максимально возможное расстояние
        return (maxM - getRast(Ob2)) / maxM;
    }

}

class VredProgDialog extends Dialog // Класс диалог. окна для редактирования классов вредных программ
{
    Label l1 = new Label("Число вредных программ:"),
            l2 = new Label("Номер вредной программы:"),
            l3 = new Label("Название:"),
            l4 = new Label("Признаки:"),
            l5 = new Label("Значение:");
    Choice C1 = new Choice(),
            C2 = new Choice(),
            C3 = new Choice(),
            C4 = new Choice();
    TextField t1 = new TextField();
    int N_Prog = 0, // Индекс редактируемой (отображаемой) вредной программы
            N_Pr = 0; //  Индекс редактируемого (отображаемого) признака
    MyOcenZashVirus Fr;

    public VredProgDialog(MyOcenZashVirus Fr, String name) {
        super(Fr, name);
        this.Fr = Fr;
        setSize(600, 400);
        setLayout(null);
        l1.setBounds(20, 40, 150, 20);
        C1.setBounds(180, 40, 40, 20);
        l2.setBounds(20, 90, 200, 20);
        C2.setBounds(20, 110, 150, 20);
        l3.setBounds(260, 90, 70, 20);
        t1.setBounds(260, 110, 100, 20);
        l4.setBounds(20, 180, 100, 20);
        l5.setBounds(380, 180, 100, 20);
        C3.setBounds(20, 210, 340, 20);
        C4.setBounds(380, 210, 120, 20);
        add(l1);
        add(l2);
        add(l3);
        add(l4);
        add(l5);
        add(C1);
        add(C2);
        add(C3);
        add(C4);
        add(t1);
        for (int i = 1; i < 100; i++)
            C1.add(String.valueOf(i));
        C1.select(Fr.N_VrPr - 1);
        for (int i = 0; i < VredProg.N; i++)
            C3.add(VredProg.Name[i]);
        for (int i = 0; i < Fr.N_VrPr; i++)
            C2.add(String.valueOf(i + 1));
        if (N_Pr < VredProg.N - 1) for (int i = 0; i < 6; i++)
            C4.add(VredProg.Value[i]);
        else for (int i = 0; i < 6; i++)
            C4.add(VredProg.Value2[i]);
        // Вывод исходных данных
        if (Fr.VrPr[N_Prog] != null) {
            t1.setText(Fr.VrPr[N_Prog].NameProg);
            C4.select(Fr.VrPr[N_Prog].Priz[N_Pr]);
        }

        setVisible(true);
    }

    public boolean handleEvent(Event e) {
        switch (e.id) {
            case Event.WINDOW_DESTROY:
                // Сохраняем отредактируемые результаты
                if (Fr.VrPr[N_Prog] == null) // Объект программа еще не создан
                {
                    String name = t1.getText(); // Имя класса вред. программы
                    int Pr[] = new int[VredProg.N];
                    Pr[N_Pr] = C4.getSelectedIndex();
                    Fr.VrPr[N_Prog] = new VredProg(Pr, name);
                } else // Объект уже существовал
                {
                    Fr.VrPr[N_Prog].NameProg = t1.getText(); // Имя класса вред. программы
                    Fr.VrPr[N_Prog].Priz[N_Pr] = C4.getSelectedIndex();
                }
                dispose(); // Закрытие окна
                return true;

        }
        return super.handleEvent(e);
    }

    public boolean action(Event e, Object obj) {
        if (e.target == C1) // Изменение числа вредных программ
        {
            int N;
            N = C1.getSelectedIndex() + 1; // Новое число вредных программ
            VredProg VP[] = new VredProg[N];
            if (Fr.N_VrPr < N)
                for (int i = 0; i < Fr.N_VrPr; i++) VP[i] = Fr.VrPr[i];
            else
                for (int i = 0; i < N; i++) VP[i] = Fr.VrPr[i];
            remove(C2);
            C2 = new Choice();
            C2.setBounds(20, 110, 150, 20);
            add(C2);
            Fr.VrPr = VP;
            Fr.N_VrPr = N;
            // Обнавляем список номеров
            for (int i = 0; i < Fr.N_VrPr; i++) C2.add((i + 1) + "");
            N_Prog = N_Pr = 0;
            // for(int i=0; i<5; i++) C4.remove(0);
            remove(C4);
            C4 = new Choice();
            C4.setBounds(380, 210, 120, 20);
            add(C4);

            for (int i = 0; i < 6; i++)
                C4.add(VredProg.Value[i]);

            if (Fr.VrPr[N_Prog] != null) {
                t1.setText(Fr.VrPr[N_Prog].NameProg);
                C4.select(Fr.VrPr[N_Prog].Priz[N_Pr]);
                C3.select(N_Pr);
            } else {
                t1.setText("");
                C4.select(0);
                C3.select(0);
            }

            return true;
        }
        if (e.target == C2) // Изменение номера редактируемой программы
        {
            // Сохраняем отредактируемые результаты
            if (Fr.VrPr[N_Prog] == null) // Объект программа еще не создан
            {
                String name = t1.getText(); // Имя класса вред. программы
                int Pr[] = new int[VredProg.N];
                Pr[N_Pr] = C4.getSelectedIndex();
                Fr.VrPr[N_Prog] = new VredProg(Pr, name);
            } else // Объект уже существовал
            {
                Fr.VrPr[N_Prog].NameProg = t1.getText(); // Имя класса вред. программы
                Fr.VrPr[N_Prog].Priz[N_Pr] = C4.getSelectedIndex();
            }
            // Выводим данные по новой редактируемой программе
            N_Prog = C2.getSelectedIndex();
            N_Pr = 0;
            C3.select(N_Pr);
            // for(int i=0; i<5; i++) C4.remove(0);
            remove(C4);
            C4 = new Choice();
            C4.setBounds(380, 210, 120, 20);
            add(C4);
            if (N_Pr < VredProg.N - 1) for (int i = 0; i < 6; i++)
                C4.add(VredProg.Value[i]);
            else for (int i = 0; i < 6; i++)
                C4.add(VredProg.Value2[i]);
            if (Fr.VrPr[N_Prog] != null) {
                t1.setText(Fr.VrPr[N_Prog].NameProg);
                C4.select(Fr.VrPr[N_Prog].Priz[N_Pr]);
            } else {
                t1.setText("");
                C4.select(0);

            }

            return true;
        }
        if (e.target == C3) // Изменение редактируемого признака
        {
            // Сохраняем отредактируемые результаты
            if (Fr.VrPr[N_Prog] == null) // Объект программа еще не создан
            {
                String name = t1.getText(); // Имя класса вред. программы
                int Pr[] = new int[VredProg.N];
                Pr[N_Pr] = C4.getSelectedIndex();
                Fr.VrPr[N_Prog] = new VredProg(Pr, name);
            } else // Объект уже существовал
            {
                Fr.VrPr[N_Prog].NameProg = t1.getText(); // Имя класса вред. программы
                Fr.VrPr[N_Prog].Priz[N_Pr] = C4.getSelectedIndex();
            }
            // Выводим данные по новому редактируемому признаку
            N_Pr = C3.getSelectedIndex();

            // for(int i=0; i<5; i++) C4.remove(0);
            remove(C4);
            C4 = new Choice();
            C4.setBounds(380, 210, 120, 20);
            add(C4);

            if (N_Pr < VredProg.N - 1) for (int i = 0; i < 6; i++)
                C4.add(VredProg.Value[i]);
            else for (int i = 0; i < 6; i++)
                C4.add(VredProg.Value2[i]);
            C4.select(Fr.VrPr[N_Prog].Priz[N_Pr]);


            // }
        /* else
            {
             t1.setText("");
            C4.select(0);
            C3.select(0);
         }*/
            return true;
        }
        if (e.target == C4) // Изменение значения редактируемого признака
        {
            // Сохраняем отредактируемые результаты
            if (Fr.VrPr[N_Prog] == null) // Объект программа еще не создан
            {
                String name = t1.getText(); // Имя класса вред. программы
                int Pr[] = new int[VredProg.N];
                Pr[N_Pr] = C4.getSelectedIndex();
                Fr.VrPr[N_Prog] = new VredProg(Pr, name);
            } else // Объект уже существовал
            {
                Fr.VrPr[N_Prog].NameProg = t1.getText(); // Имя класса вред. программы
                Fr.VrPr[N_Prog].Priz[N_Pr] = C4.getSelectedIndex();
            }


            return true;
        }
        return true;
    }
}

class TSDialog extends Dialog // Класс диалог. окна для условия функционирования ЭВМ
{
    Label
            l1 = new Label("Признаки:"),
            l2 = new Label("Значение:");
    Choice C1 = new Choice(),
            C2 = new Choice();


    int N_Pr = 0; //  Индекс редактируемого (отображаемого) признака
    MyOcenZashVirus Fr;

    public TSDialog(MyOcenZashVirus Fr, String name) {
        super(Fr, name);
        this.Fr = Fr;
        setSize(600, 300);
        setLayout(null);
        l1.setBounds(20, 60, 100, 20);
        l2.setBounds(380, 60, 100, 20);
        C1.setBounds(20, 90, 340, 20);
        C2.setBounds(380, 90, 120, 20);
        add(l1);
        add(l2);
        add(C1);
        add(C2);


        for (int i = 0; i < VredProg.N; i++)
            C1.add(VredProg.Name[i]);

        for (int i = 0; i < 6; i++)
            C2.add(VredProg.Value[i]);

        // Вывод исходных данных

        C2.select(Fr.TekS.Priz[N_Pr]);


        setVisible(true);
    }

    public boolean handleEvent(Event e) {
        switch (e.id) {
            case Event.WINDOW_DESTROY:
                // Сохраняем отредактируемые результаты
                Fr.TekS.Priz[N_Pr] = C2.getSelectedIndex();

                dispose(); // Закрытие окна
                return true;

        }
        return super.handleEvent(e);
    }

    public boolean action(Event e, Object obj) {

        if (e.target == C1) // Изменение редактируемого признака
        {
            // Сохраняем отредактируемые результаты

            Fr.TekS.Priz[N_Pr] = C2.getSelectedIndex();
            // Выводим данные по новому редактируемому признаку
            N_Pr = C1.getSelectedIndex();

            //  for(int i=0; i<5; i++) C2.remove(0);
            remove(C2);
            C2 = new Choice();
            C2.setBounds(380, 90, 120, 20);
            add(C2);
            if (N_Pr < VredProg.N - 1) for (int i = 0; i < 6; i++)
                C2.add(VredProg.Value[i]);
            else for (int i = 0; i < 6; i++)
                C2.add(VredProg.Value2[i]);
            C2.select(Fr.TekS.Priz[N_Pr]);

            return true;
        }
        if (e.target == C2) // Изменение значения редактируемого признака
        {

            Fr.TekS.Priz[N_Pr] = C2.getSelectedIndex();
            return true;
        }
        return true;
    }
}

class About extends Dialog // Окно о программе
{
    Button b1 = new Button("Закрыть");

    public About(MyOcenZashVirus Fr, String name) {
        super(Fr, name);
        setSize(600, 300);
        setLayout(null);
        b1.setBounds(280, 260, 100, 20);
        add(b1);
        setVisible(true);
    }

    public boolean handleEvent(Event e) {
        switch (e.id) {
            case Event.WINDOW_DESTROY:
                dispose(); // Закрытие окна
                return true;

        }
        return super.handleEvent(e);
    }

    public void paint(Graphics g) {
        g.drawString("Программа: Оценивание защищённости ЭВМ от программ с вредными  ", 50, 80);
        g.drawString("потенциальными последствиями.", 123, 96);
        g.drawString("Разработчик: Компанеев Ю.В.", 50, 140);
        g.drawString("2011-2012г.", 500, 250);
    }

    public boolean action(Event e, Object obj) {
        if (e.target == b1) {
            dispose(); // Закрытие окна
            return true;
        }
        return true;

    }
}

class MyHelp extends Dialog // Окно о программе
{
    Button b1 = new Button("Закрыть");

    public MyHelp(MyOcenZashVirus Fr, String name) {
        super(Fr, name);
        setSize(600, 300);
        setLayout(null);
        b1.setBounds(280, 260, 100, 20);
        add(b1);
        setVisible(true);
    }

    public boolean handleEvent(Event e) {
        switch (e.id) {
            case Event.WINDOW_DESTROY:
                dispose(); // Закрытие окна
                return true;

        }
        return super.handleEvent(e);
    }

    public void paint(Graphics g) {
        g.drawString("Для открытия нужного файла выберите в строке меню: Файл-Открыть ", 40, 80);
        g.drawString("Для сохранения нужного файла выберите в строке меню: Файл-Сохранить или Сохранить как ", 40, 110);
        g.drawString("Для редактирования данных выберите в строке меню: Редактирование", 40, 140);
        g.drawString("Для построения диаграммы выберите в строке меню: Диаграмма-Построить", 40, 170);
        g.drawString("Для выхода из программы выберите в строке меню: Файл-Выход", 40, 200);

    }

    public boolean action(Event e, Object obj) {
        if (e.target == b1) {
            dispose(); // Закрытие окна
            return true;
        }
        return true;

    }
}


public class MyOcenZashVirus extends Frame // Класс главного окна программы
{
    String nameFile; // Имя файла с исходными данными
    String title; // заголовок программы
    MenuItem It1 = new MenuItem("Открыть"),
            It2 = new MenuItem("Сохранить"),
            It3 = new MenuItem("Сохранить как"),
            It4 = new MenuItem("Выход"),
            It5 = new MenuItem("Классы вредных программ"),
            It6 = new MenuItem("Условия эксплуатации ЭВМ"),
            It7 = new MenuItem("Построить"),
            It8 = new MenuItem("Очистить"),
            It9 = new MenuItem("Справка"),
            It10 = new MenuItem("О программе");

    int N_VrPr = 5; // Число вредных программ по умолчанию
    VredProg VrPr[] = new VredProg[N_VrPr];
    VredProg TekS;
    int flagData = 0; // Флаг показывает введены исходные данные или нет
    double Mer[]; // Массив мер близостей


    public MyOcenZashVirus(String str) {
        super(str);
        title = str;
        setSize(900, 600);
        Menu m1 = new Menu("Файл"),
                m2 = new Menu("Редактирование"),
                m3 = new Menu("Диаграмма"),
                m4 = new Menu("Помощь");
        m1.add(It1);
        m1.add(It2);
        m1.add(It3);
        m1.add(It4);
        m2.add(It5);
        m2.add(It6);
        m3.add(It7);
        m3.add(It8);
        m4.add(It9);
        m4.add(It10);
        MenuBar M = new MenuBar();
        M.add(m1);
        M.add(m2);
        M.add(m3);
        M.add(m4);
        setMenuBar(M);
        int Pr[] = new int[VredProg.N];
        TekS = new VredProg(Pr, "Условия функционирования ЭВМ");

        setVisible(true);
    }

    Color getColorNum(int Num)  // Получить цвет по номеру для построения диаграммы
    {
        switch (Num) {
            case 0:
            case 9:
                return Color.blue;
            case 1:
            case 10:
                return Color.cyan;
            case 2:
            case 11:
                return Color.green;
            case 3:
            case 12:
                return Color.magenta;
            case 4:
            case 13:
                return Color.ORANGE;
            case 5:
            case 14:
                return Color.PINK;
            case 6:
            case 15:
                return Color.yellow;
            case 7:
            case 16:
                return Color.red;
            case 8:
            case 17:
                return Color.gray;
            default:
                return Color.BLACK;
        }
        //return Color.BLACK;
    }

    public void paint(Graphics g) {
        if (Mer == null) return;
        Dimension D = getSize(); // Получаем текущий размер окна
        int w = (D.width - 80) / Mer.length; // Ширина столбика в пикселях
        int hMax = D.height - 20 * Mer.length - 40 - 90; // Максимальная высота столбика соответсвует мере близости 1
        // Построение столбиков диаграммы
        for (int i = 0; i < Mer.length; i++) {
            int h = (int) (hMax * Mer[i]); // Высота столбика
            if (h == 0) h = 1;
            g.setColor(getColorNum(i)); // Устанавливаем цвет столбика
            g.fillRect(40 + i * w, D.height - 20 * Mer.length - 40 - 30 - h, w, h);
            g.fillRect(40, D.height - 20 * Mer.length + i * 20 - 20, 20, 15);
            g.drawString(VrPr[i].NameProg, 65, D.height - 20 * Mer.length - 30 + (i + 1) * 20);

        }
        // Рисование координатной сетки
        g.setColor(Color.gray);
        g.drawRect(40, 60, D.width - 80, hMax);
        g.drawString("0.0", 20, 60 + hMax);
        g.drawString("1.0", 20, 60);
        for (int i = 1; i <= 9; i++) // Рисование сетки
        {
            String str = (i / 10.) + "";
            g.drawString(str, 20, 60 + hMax - hMax * i / 10);
            g.drawLine(40, 60 + hMax - hMax * i / 10, D.width - 40, 60 + hMax - hMax * i / 10);
        }

    }

    public boolean handleEvent(Event e) {
        switch (e.id) {
            case Event.WINDOW_DESTROY:
                dispose(); // Уничтожение окна
                System.exit(0); // Выход из приложения
                return true;

        }
        return super.handleEvent(e);
    }

    public boolean action(Event e, Object what) {
        if (e.target == It1) // Открыть файл с исходными данными
        {
            FileDialog D1 = new FileDialog(this, "Открыть файл", FileDialog.LOAD);
            D1.setVisible(true);
            String str = D1.getFile();
            if (str == null)
                return true;
            String name = D1.getFile();
            if (name == null)
                return true;

            String dir = D1.getDirectory();
            dir += name;
            nameFile = dir;
            setTitle(title + " - " + nameFile);
            try  // Чтение данных из файла
            {
                FileInputStream F = new FileInputStream(nameFile);
                DataInputStream D = new DataInputStream(F);
                int N; // Число вредных программ
                N = D.readInt();
                N_VrPr = N;
                VrPr = new VredProg[N];
                int Pr[] = new int[VredProg.N];
                for (int i = 0; i < N; i++) {
                    name = D.readUTF();
                    for (int j = 0; j < VredProg.N; j++)
                        Pr[j] = D.readInt();
                    VrPr[i] = new VredProg(Pr, name);
                }

                name = D.readUTF();

                for (int j = 0; j < VredProg.N; j++)
                    Pr[j] = D.readInt();
                TekS = new VredProg(Pr, name);


                D.close();
                F.close();
                JOptionPane.showMessageDialog(this, "Исходные данные прочитаны",
                        "Сообщение", JOptionPane.INFORMATION_MESSAGE);
                flagData = 1;
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "Файл не найден или ошибка чтения данных",
                        "Сообщение", JOptionPane.ERROR_MESSAGE);
            }
            return true;

        }
        if (e.target == It2) // Сохранить данные в файле
        {
            if (nameFile == null) {
                FileDialog D1 = new FileDialog(this, "Сохранить исходные данные в файле", FileDialog.SAVE);
                D1.setVisible(true);
                String name = D1.getFile();
                String dir = D1.getDirectory();
                dir += name;
                nameFile = dir;
                setTitle(title + " - " + nameFile);
            }
            try  // Запись данных в файл
            {
                FileOutputStream F = new FileOutputStream(nameFile);
                DataOutputStream D = new DataOutputStream(F);
                D.writeInt(VrPr.length);
                for (int i = 0; i < VrPr.length; i++) {
                    D.writeUTF(VrPr[i].NameProg);
                    for (int j = 0; j < VredProg.N; j++)
                        D.writeInt(VrPr[i].Priz[j]);
                }

                D.writeUTF(TekS.NameProg);

                for (int j = 0; j < VredProg.N; j++)
                    D.writeInt(TekS.Priz[j]);


                D.close();
                F.close();
                JOptionPane.showMessageDialog(this, "Исходные данные сохранены",
                        "Сообщение", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e1) {
            }
            return true;

        }

        if (e.target == It3) // Сохранить данные в файле как
        {
            FileDialog D1 = new FileDialog(this, "Сохранить исходные данные в файле как", FileDialog.SAVE);
            D1.setVisible(true);
            String name = D1.getFile();
            String dir = D1.getDirectory();
            dir += name;
            nameFile = dir;
            setTitle(title + " - " + nameFile);
            try  // Запись данных в файл
            {
                FileOutputStream F = new FileOutputStream(nameFile);
                DataOutputStream D = new DataOutputStream(F);
                D.writeInt(VrPr.length);
                for (int i = 0; i < VrPr.length; i++) {
                    D.writeUTF(VrPr[i].NameProg);
                    for (int j = 0; j < VredProg.N; j++)
                        D.writeInt(VrPr[i].Priz[j]);
                }

                D.writeUTF(TekS.NameProg);

                for (int j = 0; j < VredProg.N; j++)
                    D.writeInt(TekS.Priz[j]);


                D.close();
                F.close();
                JOptionPane.showMessageDialog(this, "Исходные данные сохранены",
                        "Сообщение", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e1) {
            }
            return true;

        }
        if (e.target == It4) // Выход из приложения
        {
            dispose();
            System.exit(0);
        }
        if (e.target == It5) // Редактирование классов вредных программ
        {
            VredProgDialog D1 = new VredProgDialog(this, "Редактирование вредных программ");
            flagData = 1;
        }
        if (e.target == It6) // Редактирование условий эксплуатации ЭВМ
        {
            TSDialog D1 = new TSDialog(this, "Редактирование условий эксплуатации ЭВМ");
        }
        if (e.target == It7) // Построение диаграммы мер близости
        {

            // Создаем массив расстояния
            if (flagData == 0) // Исходные данные не введены
            {
                // Создаем окно сообщений об ошибке - не введены исходные данные
                JOptionPane.showMessageDialog(this, "Не введены исходные данные. Введите исходные данные",
                        "Сообщение об ошибке", JOptionPane.ERROR_MESSAGE);
                return true;


            }

            Mer = new double[VrPr.length]; // Массив мер близостей
            for (int i = 0; i < Mer.length; i++)  // Расчет мер близостей
                if (VrPr[i] != null) Mer[i] = VrPr[i].getMera(TekS); // Расчет меры близости
                else Mer[i] = 0;
            repaint(); // Перерисовка окна для построения диаграммы мер близости Вызывается метод paint

        }
        if (e.target == It8) // Очистка окна
        {
            Mer = null;
            repaint();
        }
        if (e.target == It9) // Окно со справкой
        {
            MyHelp D1 = new MyHelp(this, "Краткое руководство");
        }
        if (e.target == It10) // Окно о программе
        {
            About D1 = new About(this, "О программе");
        }
        return true;
    }

    public static void main(String[] a) {
        MyOcenZashVirus F = new MyOcenZashVirus("Оценивание защищенности ЭВМ от программ с вредными потенциальными воздействиями");

    }
}

 