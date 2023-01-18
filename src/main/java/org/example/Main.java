package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public enum SORT_MODE {
        DESCENDING("-d"), ASCENDING("-a");


        private String mode;

        SORT_MODE(String mode){
            this.mode = mode;
        }

        public String getMode() {
            return mode;
        }

    }
    public enum TYPE_MODE {
        STRING("-s"), INTEGER("-i");

        private String mode;

        TYPE_MODE(String mode){
            this.mode = mode;
        }

        public String getMode() {
            return mode;
        }
    }
    public static void main(String[] args) {
        SORT_MODE sortMode = SORT_MODE.ASCENDING;
        TYPE_MODE typeMode = null;

        ArrayList<String> files = new ArrayList<>(Arrays.asList(args));
        String outfile = null;
        ArrayList<ArrayList<String>> data = new ArrayList<>(files.size());
        ArrayList<String> line = new ArrayList<>();
        ArrayList<String> sortedList = new ArrayList<>();

        for(int i = 0; i < files.size(); ++i){
            if(files.get(i).equals(SORT_MODE.DESCENDING.getMode())){
                sortMode = SORT_MODE.DESCENDING;
                files.remove(i);
            } else if (files.get(i).equals(SORT_MODE.ASCENDING.getMode())) {
                sortMode = SORT_MODE.ASCENDING;
                files.remove(i);
            }
            if( files.get(i).equals(TYPE_MODE.INTEGER.getMode())){
                typeMode = TYPE_MODE.INTEGER;
                files.remove(i);
            } else if ( files.get(i).equals(TYPE_MODE.STRING.getMode())) {
                typeMode = TYPE_MODE.STRING;
                files.remove(i);
            }
            if(files.get(i).equals("out.txt")){
                outfile = files.get(i);
                files.remove(i);
            }
        }
        if(typeMode == null){
            System.out.println("please select type sort");
            return;
        }
        if(files.isEmpty()){
            System.out.println("no input files");
            return;
        }
        sortedList.clear();

        for(int i = 0; i < files.size(); ++i){
            data.add(isSorted(files.get(i), typeMode));
        }

        do{
            line.clear();

            if(!data.isEmpty()){
                for(int i = 0; i < data.size(); ++i){
                    if(!data.get(i).isEmpty()) {
                        switch (sortMode){
                            case ASCENDING:{
                                line.add(data.get(i).get(0));
                                break;
                            }
                            case DESCENDING:{
                                line.add(data.get(i).get(data.get(i).size() - 1));
                                break;
                            }
                            default:{
                                break;
                            }
                        }
                    }
                }
            }

            if(!line.isEmpty()) {

                sortedList.add(findWhatNeed(line, sortMode, typeMode));

                for(int i = 0; i < data.size(); ++i){
                    if(data.get(i).contains(findWhatNeed(line, sortMode, typeMode))){
                        data.get(i).remove(findWhatNeed(line, sortMode, typeMode));
                        break;
                    }
                }
            }
        }while(!data.isEmpty() && !line.isEmpty());

        for(int i = 0; i < sortedList.size(); ++i){
            System.out.print(sortedList.get(i) + " ");
        }


        try(OutputStream f = new FileOutputStream(outfile);
            OutputStreamWriter writer = new OutputStreamWriter(f);
            BufferedWriter out = new BufferedWriter(writer)){

            for(int i = 0; i < sortedList.size(); i++) {
                out.write(sortedList.get(i) + "\n");
                out.flush();
            }
        }catch(Exception e){
            System.out.println("\noutput file is not found, sortedList will be displayed on terminal");
            System.out.println(sortedList);
        }
    }

    static String findWhatNeed( ArrayList<String> block, SORT_MODE sortmode, TYPE_MODE typeMode){
        if(!block.isEmpty()) {

            String needtofind;
            switch (sortmode){
                case ASCENDING:{
                    needtofind = block.get(0);
                    break;
                }
                case DESCENDING:{
                    needtofind = block.get(block.size() - 1);
                    break;
                }
                default:{
                    needtofind = "";
                    break;
                }
            }

            for (int i = 0; i < block.size(); ++i) {
                switch (sortmode){
                    case ASCENDING:{
                        switch (typeMode){
                            case STRING:{
                                if (needtofind.compareTo(block.get(i)) >= 0 ) {
                                    needtofind = block.get(i);
                                }
                                break;
                            }
                            case INTEGER:{
                                if(Integer.valueOf(needtofind) > Integer.valueOf(block.get(i))){
                                    needtofind = block.get(i);
                                }
                                break;
                            }
                            default:
                                break;
                        }
                        break;
                    }
                    case DESCENDING:{
                        switch (typeMode){
                            case STRING:{
                                if (needtofind.compareTo(block.get(i)) <= 0) {
                                    needtofind = block.get(i);
                                }
                                break;
                            }
                            case INTEGER:{
                                if(Integer.valueOf(needtofind) < Integer.valueOf(block.get(i))){
                                    needtofind = block.get(i);
                                }
                                break;
                            }
                            default:
                                break;
                        }
                        break;
                    }
                    default:{
                        break;
                    }
                }
            }
            return String.valueOf(needtofind);
        }
        return null;
    }

    static ArrayList<String> isSorted(String path, TYPE_MODE typeMode){
        String line;
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {

            if(typeMode == TYPE_MODE.STRING){
                ArrayList<String> linesString = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    linesString.add(line);
                }

                String[] data = linesString.toArray(new String[]{});
                Arrays.sort(data);
                linesString.clear();
                for(int i = 0; i < data.length; ++i){
                    linesString.add(data[i]);
                }

                return linesString;
            }else{
                ArrayList<Integer> linesInteger = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    linesInteger.add(Integer.valueOf(line));
                }

                Integer[] data = linesInteger.toArray(new Integer[]{});
                Arrays.sort(data);
                linesInteger.clear();
                ArrayList<String> lines1 = new ArrayList<>();
                for(int i = 0; i < data.length; ++i){
                    lines1.add(String.valueOf(data[i]));
                }
                return lines1;
            }




        }catch (IOException e){
            return new ArrayList<>();
        }
    }
}