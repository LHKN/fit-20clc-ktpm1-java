//package PA_Dict_20127679;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DApp implements ItemListener{
    //main
    //use swingUI/Console
    //draw cmd from slangDict
    private static slangDict sd;
    private static String fi = "slang.txt";
    private static String fo = "slang2.txt";

    //console
    public static void ConsoleUI() throws IOException{
        Scanner input = new Scanner(System.in);
        sd = new slangDict();
        while(true){
            System.out.println("Enter 1 to end the program;");
    
            System.out.println("Enter 2 to search a slang's definition using slang;");
            System.out.println("Enter 3 to search a slang using slang's definition;");
            System.out.println("Enter 4 to view search history;");
            System.out.println("Enter 5 to add a slang;");
            System.out.println("Enter 6 to edit a slang;");
            System.out.println("Enter 7 to delete a slang;");
            System.out.println("Enter 8 to reset slang list;");
            System.out.println("Enter 9 to randomize a slang (On this day slang word);");
            System.out.println("Enter 10 to play minigame ( ._.)! Guess the definition of the slang;");
            System.out.println("Enter 11 to play minigame (._. )! Guess the slang.");
    
            System.out.println("Enter 12 to view dictionary.");
    
            int option = input.nextInt();
            System.out.println();
    
            switch(option){
                case 1: //end program
                {
                    input.close();
                    System.exit(0);
                    break;
                }
                case 2:
                {
                    boolean check = true;
                    while(check){
                        System.out.print("Enter slang: ");
                        String word = input.nextLine();
                        word = input.nextLine();
                        sd.searchWord(word);
    
                        System.out.println("Enter another slang? Enter 1 for Yes, other numbers for No: ");
                        int opt = input.nextInt();
                        if (opt!=1) check=!check;
                        System.out.println();
                    }
                    break;
                }
                case 3:
                {
                    boolean check = true;
                    while(check){
                        System.out.print("Enter slang's definition: ");
                        String word = input.nextLine();
                        word = input.nextLine();
                        sd.searchDefinition(word);
    
                        System.out.println("Enter another slang's definition? Enter 1 for Yes, other numbers for No: ");
                        int opt = input.nextInt();
                        if (opt!=1) check=!check;
                        System.out.println();
                    }
                    break;
                }
                case 4:
                {
                    sd.viewSearchHistory();
                    break;
                }
                case 5:
                {
                    System.out.print("Enter slang to add: ");
                    String word = input.nextLine();
                    word = input.nextLine();
                    sd.addSlang(word);
                    break;
                }
                case 6:
                {
                    System.out.print("Enter slang to edit: ");
                    String word = input.nextLine();
                    word = input.nextLine();
                    sd.editSlang(word);
                    break;
                }
                case 7:
                {
                    System.out.print("Enter slang to delete: ");
                    String word = input.nextLine();
                    word = input.nextLine();
                    sd.deleteSlang(word);
                    break;
                }
                case 8: 
                {
                    sd.resetDict();
                    break;
                }
                case 9:
                {
                    sd.randomSlang();
                    break;
                }
                case 10:
                {
                    sd.wordMinigame();
                    break;
                }
                case 11:
                {
                    sd.definitionMinigame();
                    break;
                }
                case 12: //debug zone
                {
                    sd.viewDictConsole();
                    break;
                }
                default:
                System.out.println("Option unvailable. Choose again!");       
            }
        }
    }

    //SwingUI
    JPanel cards;
    String options1[] = {"Search by slang word","Search by definition"};
    String options2[] = {"Search slang","View search history","Add Slang","Edit Slang","Delete Slang","Reset List","Random Slang","Minigame 1","Minigame 2"};
    String options3[] = {"Edit slang word","Edit definition"};
    Object[] options4 = {"Confirm","Cancel"};
  
    public void addComponentToPane(Container pane){
        //SEARCH
        JPanel searchpanel = new JPanel();

        JButton s_confirm_btn = new JButton("CONFIRM");
        JComboBox<String> scb = new JComboBox<String>(options1);
        scb.setMaximumSize(new Dimension(200,30));
        scb.setEditable(false);
        JTextField s_text = new JTextField();
        s_text.setMaximumSize(new Dimension(200,30)); 
        
        searchpanel.add(scb, BorderLayout.EAST);
        searchpanel.add(s_text, BorderLayout.SOUTH);
        searchpanel.add(s_confirm_btn, BorderLayout.SOUTH);

        searchpanel.setLayout(new BoxLayout(searchpanel, BoxLayout.Y_AXIS));

        JPanel s_result = new JPanel();

        DefaultListModel<String> s_model = new DefaultListModel<String>();
         
        JList<String> s_list = new JList<String>(s_model);
        s_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        s_list.setLayoutOrientation(JList.VERTICAL);
        s_list.setSelectedIndex(0);
        s_list.setVisibleRowCount(5);
        s_list.setVisible(true);

        JScrollPane ssp = new JScrollPane(s_list);
        ssp.setPreferredSize(new Dimension(500,500));
        s_result.add(ssp);

        searchpanel.add(s_result);

        cards = new JPanel(new CardLayout());
        cards.add(searchpanel, options2[0]);
        
        //VIEW
        JPanel viewpanel = new JPanel();
        JLabel v_label = new JLabel("SEARCH HISTORY [OwO ]");
        viewpanel.add(v_label);

        DefaultListModel<String> v_history = new DefaultListModel<String>();

        JList<String> v_list = new JList<String>(v_history);
        v_list.setLayoutOrientation(JList.VERTICAL);
        v_list.setVisibleRowCount(5);

        JScrollPane vsp = new JScrollPane(v_list);
        vsp.setPreferredSize(new Dimension(500,500));
        viewpanel.add(vsp);

        viewpanel.setLayout(new BoxLayout(viewpanel, BoxLayout.Y_AXIS));

        cards.add(viewpanel, options2[1]);

        //ADD
        JPanel addpanel = new JPanel();
        JPanel a_p1 = new JPanel();
        JPanel a_p2 = new JPanel();

        JLabel a_label = new JLabel("ADD A SLANG WORD ");
        addpanel.add(a_label);
        
        JLabel a_label1 = new JLabel("Enter slang word: ");
        JTextField a_text1 = new JTextField();
        a_text1.setMaximumSize(new Dimension(500,30)); 
        a_p1.add(a_label1);
        a_p1.add(a_text1);
        a_p1.setLayout(new BoxLayout(a_p1, BoxLayout.X_AXIS));
        
        JLabel a_label2 = new JLabel("Enter definition: ");
        JTextField a_text2 = new JTextField();
        a_text2.setMaximumSize(new Dimension(510,30));
        a_p2.add(a_label2);
        a_p2.add(a_text2);
        a_p2.setLayout(new BoxLayout(a_p2, BoxLayout.X_AXIS));
        
        JButton a_confirm_btn = new JButton("CONFIRM");
        
        addpanel.add(a_p1);
        addpanel.add(a_p2);
        addpanel.add(a_confirm_btn);

        addpanel.setLayout(new BoxLayout(addpanel, BoxLayout.Y_AXIS));

        cards.add(addpanel, options2[2]);

        //EDIT
        JPanel editpanel = new JPanel();
        JPanel e_p1 = new JPanel();

        JLabel e_label = new JLabel("EDIT A SLANG WORD ");
        editpanel.add(e_label);

        JLabel e_label1 = new JLabel("Enter slang word: ");
        JTextField e_text = new JTextField();
        e_text.setMaximumSize(new Dimension(500,30));
        e_p1.add(e_label1);
        e_p1.add(e_text);
        e_p1.setLayout(new BoxLayout(e_p1, BoxLayout.X_AXIS));

        JComboBox<String> ecb = new JComboBox<String>(options3);
        ecb.setMaximumSize(new Dimension(200,30));
        ecb.setEditable(false);

        JButton e_confirm_btn = new JButton("CONFIRM");
        
        editpanel.add(e_p1);
        editpanel.add(ecb);
        editpanel.add(e_confirm_btn);

        editpanel.setLayout(new BoxLayout(editpanel, BoxLayout.Y_AXIS));

        cards.add(editpanel, options2[3]);

        //DELETE
        JPanel deletepanel = new JPanel();
        JPanel d_p1 = new JPanel();

        JLabel d_label = new JLabel("DELETE A SLANG WORD ");
        deletepanel.add(d_label);

        JLabel d_label1 = new JLabel("Enter slang word: ");
        JTextField d_text = new JTextField();
        d_text.setMaximumSize(new Dimension(500,30)); 
        d_p1.add(d_label1);
        d_p1.add(d_text);
        d_p1.setLayout(new BoxLayout(d_p1, BoxLayout.X_AXIS));

        JButton d_confirm_btn = new JButton("CONFIRM");
        
        deletepanel.add(d_p1);
        deletepanel.add(d_confirm_btn);

        deletepanel.setLayout(new BoxLayout(deletepanel, BoxLayout.Y_AXIS));

        cards.add(deletepanel, options2[4]);

        //RANDOM
        JPanel randompanel = new JPanel();
        JLabel r_label = new JLabel(" ON THIS DAY SLANG WORD (^o^)/ ");
        randompanel.add(r_label);

        JTextField r_text = new JTextField();
        r_text.setMaximumSize(new Dimension(500,30)); 
        
        randompanel.add(r_text);
        //randompanel.setLayout(new BoxLayout(randompanel, BoxLayout.Y_AXIS));

        cards.add(randompanel, options2[6]);

        //MNG1
        JPanel mng1panel = new JPanel();
        JLabel m1_label = new JLabel(" MINIGAME 1 (^o^)/ ");
        mng1panel.add(m1_label);

        JTextField m1_text = new JTextField();
        m1_text.setMaximumSize(new Dimension(500,30)); 
        
        mng1panel.add(m1_text);

        cards.add(mng1panel, options2[7]);

        //MNG2
        JPanel mng2panel = new JPanel();
        JLabel m2_label = new JLabel(" MINIGAME 2 (^o^)/ ");
        mng2panel.add(m2_label);

        JTextField m2_text = new JTextField();
        m2_text.setMaximumSize(new Dimension(500,30)); 
        
        mng2panel.add(m2_text);

        cards.add(mng2panel, options2[8]);

        //BUTTONS
        JPanel buttons = new JPanel();

        JButton search_btn = new JButton(options2[0]);
        search_btn.setMaximumSize(new Dimension(200,50));
        buttons.add(search_btn);

        JButton view_btn = new JButton(options2[1]);
        view_btn.setMaximumSize(new Dimension(200,50));
        buttons.add(view_btn);

        JButton add_btn = new JButton(options2[2]);
        add_btn.setMaximumSize(new Dimension(200,50));
        buttons.add(add_btn);

        JButton edit_btn = new JButton(options2[3]);
        edit_btn.setMaximumSize(new Dimension(200,50));
        buttons.add(edit_btn);
                
        JButton delete_btn = new JButton(options2[4]);
        delete_btn.setMaximumSize(new Dimension(200,50));
        buttons.add(delete_btn);

        JButton reset_btn = new JButton(options2[5]);
        reset_btn.setMaximumSize(new Dimension(200,50));
        buttons.add(reset_btn);

        JButton random_btn = new JButton(options2[6]);
        random_btn.setMaximumSize(new Dimension(200,50));
        buttons.add(random_btn);

        JButton mng1_btn = new JButton(options2[7]);
        mng1_btn.setMaximumSize(new Dimension(200,50));
        buttons.add(mng1_btn);

        JButton mng2_btn = new JButton(options2[8]);
        mng2_btn.setMaximumSize(new Dimension(200,50));
        buttons.add(mng2_btn);

        s_confirm_btn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                s_model.clear();
                String t = s_text.getText();
                if (!t.equals("")){
                    if (scb.getSelectedIndex()==0){
                        ArrayList<ArrayList<String>> aa = sd.searchWord(t);
                        if (aa!=null){
                            for (ArrayList<String> a:aa){
                                String str="";
                                for (String s:a){
                                    str+=s+"; ";
                                }
                                s_model.addElement(str);
                            }
                        }
                        else{
                            //noti not found
                            JFrame noti = new JFrame("Notification");
                            JOptionPane.showMessageDialog(noti, "This slang is not in this dictionary (UmU)...");     
                        }
                    }
                    else if(scb.getSelectedIndex()==1){
                        sd.searchDefinition(t);
                        Set<String> aa = sd.searchDefinition(t);
                        if (aa!=null){
                            for (String s:aa){
                                s_model.addElement(s);
                            }
                        }
                        else{
                            //noti not found
                            JFrame noti = new JFrame("Notification");
                            JOptionPane.showMessageDialog(noti, "We can not find the definition in this dictionary (UmU)...");
                        }
                    }
                }else{
                    //noti pls enter sth
                    JFrame noti = new JFrame("Notification");
                    JOptionPane.showMessageDialog(noti, "The text field is empty! Please enter something (UmU)...");
                }
            }
        });

        search_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards,options2[0]);
            }
        });

        view_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                v_history.clear();
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards,options2[1]);

                HashMap<String,ArrayList<ArrayList<String>>> hm = sd.viewSearchHistory();
                if (hm!=null){
                    for (String k:hm.keySet()){
                        for (ArrayList<String> aa:hm.get(k)){
                            String s = k + " : ";
                            for (String a:aa){
                                s+=a+"; ";
                            }
                            v_history.addElement(s);
                        }
                    }
                }else{
                    JFrame noti = new JFrame("Notification");
                    JOptionPane.showMessageDialog(noti, "You haven't searched any slangs (ehe)>[UwU ]");
                }
            }
        });
        
        a_confirm_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0){
                String w = a_text1.getText();
                String d = a_text2.getText();

                if(w.equals("")){
                    JFrame noti = new JFrame("Notification");
                    JOptionPane.showMessageDialog(noti, "The slang word text field is empty! Please enter something (UmU)...");
                    return;
                }

                if(sd.foundWord(w)){
                    JFrame noti = new JFrame("Options");
                    Object[] options1 = { "Overwrite definition", "Add to definition","Duplicate slang","Cancel" };
                    int i = JOptionPane.showOptionDialog(noti, "The slang word already exists!! {-_- } Would you like to: ", "Options", 0, 0, null, options1, null);
                    if (i==3) return;

                    int idx = 0;

                    if(sd.checkDup(w)){
                        JPanel a_result = new JPanel();
    
                        DefaultListModel<String> a_model = new DefaultListModel<String>();
                        ArrayList<String> aa = new ArrayList<>();

                        JList<String> a_list = new JList<String>(a_model);
                        a_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                        a_list.setLayoutOrientation(JList.VERTICAL);
                        a_list.setSelectedIndex(0);
                        a_list.setVisibleRowCount(5);
                        a_list.setVisible(true);
    
                        JScrollPane asp = new JScrollPane(a_list);
                        asp.setPreferredSize(new Dimension(500,500));
                        a_result.add(asp);

                        ArrayList<ArrayList<String>> old = sd.getDefinition(w);
                        
                        for (ArrayList<String> a:old){
                            String str=w+": ";
                            for (String s:a){
                                str+=s+"; ";
                            }
                            a_model.addElement(str);
                            aa.add(str);
                        }

                        int ii = JOptionPane.showOptionDialog(noti, new Object[]{"The entered slang is duplicated. Please select the slang you want to work with:",a_result}, "Options", 0, 0, null, options4, null);
                        if (ii == 0){
                            idx = a_list.getSelectedIndex();
                        }
                        else{
                            return;
                        }
                    }

                    JFrame n = new JFrame("Notification");
                    switch (i){
                        case 0:
                        {
                            try{
                                sd.overwrite(w, d, idx);
                                JOptionPane.showMessageDialog(n, "Updated!! {~_~ }");
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }
                            break;
                        } 
                        case 1:
                        {
                            try{
                                sd.append(w, d, idx);
                                JOptionPane.showMessageDialog(n, "Updated!! {~_~ }");
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }
                            break;
                        }
                        case 2:
                        {
                            try{
                                sd.duplicate(w, idx);
                                JOptionPane.showMessageDialog(n, "Updated!! {~_~ }");
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }
                            break;
                        }
                        default:
                        return;
                    }

                }
                else{
                    try{
                        sd.add(w,d);
                        JFrame noti = new JFrame("Notification");
                        JOptionPane.showMessageDialog(noti, "Updated!! {~_~ }");
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        add_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards,options2[2]);
            }
        });

        e_confirm_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String w = e_text.getText();
                JFrame noti = new JFrame("Notification");
                
                if(w.equals("")){
                    JOptionPane.showMessageDialog(noti, "The slang word text field is empty! Please enter something (UmU)...");
                    return;
                }
                
                if(!sd.foundWord(w)){
                    //noti not found
                    JOptionPane.showMessageDialog(noti, "This slang is not in this dictionary (UmU)...");     
                    return;
                }

                int idx = 0;
                
                JPanel e_result = new JPanel();

                ArrayList<String> ea = new ArrayList<>();
                DefaultListModel<String> e_model = new DefaultListModel<String>();
                 
                JList<String> e_list = new JList<String>(e_model);
                e_list.setLayoutOrientation(JList.VERTICAL);
                e_list.setVisibleRowCount(5);
        
                JScrollPane esp = new JScrollPane(e_list);
                esp.setPreferredSize(new Dimension(500,500));
                e_result.add(esp);

                ArrayList<ArrayList<String>> old = sd.getDefinition(w);

                if(sd.checkDup(w)){
                    for (ArrayList<String> a:old){
                        String str=w+": ";
                        for (String s:a){
                            str+=s+"; ";
                        }
                        e_model.addElement(str);
                        ea.add(str);
                    }

                    int ii = JOptionPane.showOptionDialog(noti, new Object[]{"The entered slang is duplicated. Please select the slang you want to work with:",e_result}, "Options", 0, 0, null, options4, null);
                    if (ii == 0){
                        idx = e_list.getSelectedIndex();
                    }else{
                        return;
                    }
                }
                
                switch(ecb.getSelectedIndex()){
                    case 0: //edit word
                    {
                        int ii = JOptionPane.showOptionDialog(noti, new Object[]{"This will replace the slang word and keep the old definition. Proceed?"}, "Edit word", 0, 0, null, options4, null);
                        if(ii!=0) return;

                        String nw = JOptionPane.showInputDialog(noti, "Enter new slang word: ");

                        if(nw.equals("")){
                            JOptionPane.showMessageDialog(noti, "The text field is empty! Please enter something (UmU)...");
                            return;
                        }

                        if(sd.foundWord(nw)){
                            JOptionPane.showMessageDialog(noti, "The slang word already exists!! Please go to add slang if you want to duplicate it... {-_- }");
                            return;
                        }

                        try{
                            sd.editWord(w, nw, idx);
                            JOptionPane.showMessageDialog(noti, "Updated!! {~_~ }");
                            return;
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    case 1: //edit definition
                    {
                        int ii = JOptionPane.showOptionDialog(noti, new Object[]{"This will keep the old slang word and change the definition. Proceed?"}, "Edit word", 0, 0, null, options4, null);
                        if(ii!=0) return;

                        String d = JOptionPane.showInputDialog(noti, "Enter new definition: ");

                        if(d.equals("")){
                            JOptionPane.showMessageDialog(noti, "The text field is empty! Please enter something (UmU)...");
                            return;
                        }

                        Object[] options1 = { "Overwrite definition", "Add to definition", "Cancel" };
                        int i = JOptionPane.showOptionDialog(noti, "Would you like to: ", "Options", 0, 0, null, options1, null);
                        if (i==3) return;


                        switch (i){
                            case 0:
                            {
                                int n = -1;
                                if(sd.checkMult(w,idx)){
                                    e_model.clear();
                                    ArrayList<String> a = old.get(idx);
                                        for (String s:a){
                                            e_model.addElement(s);
                                            ea.add(s);
                                        }
                                    }
                                    int iii = JOptionPane.showOptionDialog(noti, new Object[]{"There are multiple definitions. Please select the definition you want to work with and click Confirm, or click Cancle to overwrite all:",e_result}, "Options", 0, 0, null, options4, null);
                                    if (iii == 0){
                                        n = e_list.getSelectedIndex();
                                }

                                if (n!=-1){
                                    try{
                                        sd.editDefinition(w, d, idx, n);
                                        JOptionPane.showMessageDialog(noti, "Updated!! {~_~ }");
                                        return;
                                    }
                                    catch(Exception e){
                                        e.printStackTrace();
                                    }
                                    break;
                                }

                                try{
                                    sd.overwrite(w, d, idx);
                                    JOptionPane.showMessageDialog(noti, "Updated!! {~_~ }");
                                    return;
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            } 
                            case 1:
                            {
                                try{
                                    sd.append(w, d, idx);
                                    JOptionPane.showMessageDialog(noti, "Updated!! {~_~ }");
                                    return;
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                                break;
                            }
                            default:
                            return;
                        }
                    }
                    default:
                    return;
                }
            }
        });

        edit_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards,options2[3]);
            }
        });
        
        d_confirm_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {  
                String w = d_text.getText();
                JFrame noti = new JFrame("Notification");

                if(w.equals("")){
                    JOptionPane.showMessageDialog(noti, "The slang word text field is empty! Please enter something (UmU)...");
                    return;
                }

                if(!sd.foundWord(w)){
                    //noti not found
                    JOptionPane.showMessageDialog(noti, "This slang is not in this dictionary (UmU)...");     
                    return;
                }
                
                int idx = 0;
                
                JPanel e_result = new JPanel();

                ArrayList<String> ea = new ArrayList<>();
                DefaultListModel<String> e_model = new DefaultListModel<String>();
                 
                JList<String> e_list = new JList<String>(e_model);
                e_list.setLayoutOrientation(JList.VERTICAL);
                e_list.setVisibleRowCount(5);
        
                JScrollPane esp = new JScrollPane(e_list);
                esp.setPreferredSize(new Dimension(500,500));
                e_result.add(esp);

                ArrayList<ArrayList<String>> old = sd.getDefinition(w);

                if(sd.checkDup(w)){
                    for (ArrayList<String> a:old){
                        String str=w+": ";
                        for (String s:a){
                            str+=s+"; ";
                        }
                        e_model.addElement(str);
                        ea.add(str);
                    }

                    int i = JOptionPane.showOptionDialog(noti, new Object[]{"The entered slang is duplicated. Please select the slang you want to work with:",e_result}, "Options", 0, 0, null, options4, null);
                    if (i == 0){
                        idx = e_list.getSelectedIndex();
                    }
                    else{
                        return;
                    }
                }

                int i = JOptionPane.showOptionDialog(noti,"Do you want to delete this slang?", "Options", 0, 0, null, options4, null);
                if(i==0){
                    try{
                        sd.delete(w, idx);
                        JOptionPane.showMessageDialog(noti, "Slang is deleted! <OAO >");
                        return;
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        delete_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards,options2[4]);
            }
        });

        reset_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                sd.inputDict(fi);
                JFrame noti = new JFrame("Notification");
                JOptionPane.showMessageDialog(noti, "List is reset!! <0A0 >");
                try{
                    sd.outputDict(fo);
        
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        random_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards,options2[6]);
                
                //doStuff();
            }
        });

        mng1_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards,options2[7]);
                
                //doStuff();
            }
        });

        mng2_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards,options2[8]);
                
                //doStuff();
            }
        });

        //

        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

        pane.add(cards, BorderLayout.CENTER);
        pane.add(buttons, BorderLayout.EAST);        
    }

    public void itemStateChanged(ItemEvent evt) 
    {
        // CardLayout cl = (CardLayout)(cards.getLayout());
        // cl.show(cards, (String)evt.getItem());
    }

    private static void createAndShowGUI() 
    {
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("Slang Dictionary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        DApp demo = new DApp();
        demo.addComponentToPane(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
    }

    DApp() {
        sd = new slangDict();
    }

    public static void main(String args[]) throws IOException{
        //ConsoleUI();

        javax.swing.SwingUtilities.invokeLater(new Runnable() 
        {
           public void run() 
           {
            createAndShowGUI();
           }
        });
    }
    //main referenced from teacher's Beeper demonstration (5%)
}