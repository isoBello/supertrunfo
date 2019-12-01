class Game{
    static main(args){
        Cards cards = new Cards()
        Player player = new Player(cards.playerCards)
        Computer computer = new Computer(cards.computerCards)
        Table table = new Table()

        def playerCards = player.playersCards
        def computerCards = computer.playersCards
        def tableCards = table.playersCards

        def playerCard = playerCards.take(1)
        def computerCard = computerCards.take(1)
        
        def whoPlays = 0
        def index = 0

        def choices = ['Confiabilidade', 'Aprendizado', 'Eficiencia', 'Portabilidade', 'Reusabilidade']
        def options = ['maior', 'menor']
        
        while(player.playersCards.size() > 0 || computer.playersCards.size() > 0){
            def keyPlayer 
            def valuePlayer = []
            def keyComp 
            def valueComp = []

            playerCard.each{k -> keyPlayer = "$k.key"}
            playerCard.each{v -> valuePlayer = "$v.value"}
            computerCard.each{k -> keyComp = "$k.key"}
            computerCard.each{v -> valueComp = "$v.value"}  

            println("\nRound " + whoPlays)
            println("\nTable Cards: " + table.playersCards.size())

            if(whoPlays % 2 == 0){
                printCard("Player", keyPlayer, valuePlayer)
                
                def choice = System.console().readLine '\nEscolha um atributo: '
                def option = System.console().readLine '\nEscolha maior ou menor: '
                
                /*while(option.equalsIgnoreCase("maior") == false && option.equalsIgnoreCase("menor") == false){
                    option = System.console().readLine 'Opçao errada! Escolha maior ou menor: '
                }*/
                
                playerCard.each{v -> if("$v.value".contains(choice) == true) index = "$v.value".indexOf(choice)+choice.length()+1 else print('Digitou a caracteristica errada, perdeu a vez!')}

                if(option.equalsIgnoreCase("maior") == true){
                    printCard("Oponente", keyComp, valueComp)
                    if(valuePlayer.getAt(index) > valueComp.getAt(index)){
                        playerWins(player, computer, table, tableCards, keyComp, valueComp)
                    }else if(valuePlayer.getAt(index) < valueComp.getAt(index)){
                        computerWins(player, computer, table, tableCards, keyPlayer, valuePlayer)
                        whoPlays += 1
                    }else{
                        tie(player, computer, table, keyPlayer, valuePlayer, keyComp, valueComp)
                    }
                }else if (option.equalsIgnoreCase("menor") == true){
                    printCard("Oponente", keyComp, valueComp)
                    if(valuePlayer.getAt(index) < valueComp.getAt(index)){
                        playerWins(player, computer, table, tableCards, keyComp, valueComp)                         
                    }else if(valuePlayer.getAt(index) > valueComp.getAt(index)){
                        computerWins(player, computer, table, tableCards, keyPlayer, valuePlayer)
                        whoPlays += 1
                    }else{
                        tie(player, computer, table, keyPlayer, valuePlayer, keyComp, valueComp)
                    }
                }
            }else{
                printCard("Computer", keyComp, valueComp)

                def choice = Math.abs(new Random().nextInt() % (choices.size - 0))
                def option = Math.abs(new Random().nextInt() % (options.size - 0))

                computerCard.each{v -> if("$v.value".contains(choice.toString()) == true) index = "$v.value".indexOf(choice.toString())+choice.toString().length()+1}

                if(options.getAt(option).equalsIgnoreCase("maior") == true){
                    printCard("Oponente", keyPlayer, valuePlayer)
                    if(valueComp.getAt(index) > valuePlayer.getAt(index)){
                        computerWins(player, computer, table, tableCards, keyPlayer, valuePlayer)                        
                    }else if(valueComp.getAt(index) < valuePlayer.getAt(index)){
                        playerWins(player, computer, table, tableCards, keyComp, valueComp)
                        whoPlays += 1
                    }else{
                        tie(player, computer, table, keyPlayer, valuePlayer, keyComp, valueComp)
                    }
                }else if (options.getAt(option).equalsIgnoreCase("menor") == true){
                    printCard("Oponente", keyPlayer, valuePlayer)
                    if(valueComp.getAt(index) < valuePlayer.getAt(index)){
                        computerWins(player, computer, table, tableCards, keyPlayer, valuePlayer)                        
                    }else if(valueComp.getAt(index) > valuePlayer.getAt(index)){                     
                        playerWins(player, computer, table, tableCards, keyComp, valueComp)
                        whoPlays += 1 
                    }else{
                        tie(player, computer, table, keyPlayer, valuePlayer, keyComp, valueComp)
                    }
                }

            }

            playerCard = null
            computerCard = null
            index = 0
            playerCard = player.playersCards.take(1)
            computerCard = computer.playersCards.take(1)           
        }  
    }

    static def printCard(name, key, value){
        println("\n$name Card")
        println("\n[$key]")
        println(value)
    }

    static def playerWins(player, computer, table, tableCards, key, value){
        println("Win!")
        player.addCard(key, value)
        computer.removeCard(key)

        def keyTable = []
        def valueTable = []
                        
        if(tableCards.size() > 0){
            while(table.playersCards.size() > 0){
                def tableCard = table.playersCards.take(1)
                tableCard.each{k -> keyTable = "$k.key"}
                tableCard.each{v -> valueTable = "$v.value"}
                table.removeCard(keyTable)
                player.addCard(keyTable, valueTable)
            }
        }
    }

    static def computerWins(player, computer, table, tableCards, key, value){
        println('Lost!')
        computer.addCard(key, value)
        player.removeCard(key)

        def keyTable 
        def valueTable = []

        if(tableCards.size() > 0){
            while(table.playersCards.size() > 0){
                def tableCard = table.playersCards.take(1)
                tableCard.each{k -> keyTable = "$k.key"}
                tableCard.each{v -> valueTable = "$v.value"}
                table.removeCard(keyTable)
                computer.addCard(keyTable, valueTable)
            }
        }
    }

    static def tie(player, computer, table, fstkey, fstvalue, sndkey, sndvalue){
        println('Tie!')
        player.removeCard(fstkey)
        computer.removeCard(sndkey)
        table.addCard(fstkey, fstvalue)
        table.addCard(sndkey, sndvalue)
    }
}

abstract class Players{
    def playersCards = [:]

    abstract def addCard(key, value)
    abstract def removeCard(key)
}


class Player extends Players{
    Player(cards){
        playersCards = cards
    }

    def addCard(key, value){
        playersCards.put(key, value)
    }

    def removeCard(key){
        playersCards = playersCards.findAll{it.key != key}
    }
}

class Computer extends Players{
    Computer(cards){
        playersCards = cards
    }

    def addCard(key, value){
        playersCards.put(key, value)
    }

    def removeCard(key){
        playersCards = playersCards.findAll{it.key != key}
    }
}

class Table extends Players{
    def addCard(key, value){
        playersCards.put(key, value)
    }

    def removeCard(key){
        playersCards = playersCards.findAll{it.key != key}
    }
}

class Cards{
    def cards = [:]
    def playerCards = [:]
    def computerCards = [:]
    
    Cards(){
        Java java = new Java()
        cards.put("Java", java.attributes)
        
        C c = new C()
        cards.put("C", c.attributes)
        
        Python python = new Python()
        cards.put("Python", python.attributes)
        
        CPlus cplus = new CPlus()
        cards.put("C++", cplus.attributes)
            
        CSharp csharp = new CSharp()
        cards.put("C#", csharp.attributes)
        
        DotNet dotnet = new DotNet()
        cards.put("Visual Basic .NET", dotnet.attributes)
        
        JavaScript javas = new JavaScript()
        cards.put("JavaScript", javas.attributes)
        
        PHP php = new PHP()
        cards.put("PHP", php.attributes)
        
        SQL sql = new SQL()
        cards.put("SQL", sql.attributes)
        
        Swift swift = new Swift()
        cards.put("Swift", swift.attributes)
        
        Ruby ruby = new Ruby()
        cards.put("Ruby", ruby.attributes)
        
        ObjectiveC objective = new ObjectiveC()
        cards.put("Objective-C", objective.attributes)
        
        Delphi delphi = new Delphi()
        cards.put("Delphi", delphi.attributes)
        
        Groovy groovy = new Groovy()
        cards.put("Groovy", groovy.attributes)
        
        Assembly assembly = new Assembly()
        cards.put("Assembly", assembly.attributes)
        
        R r = new R()
        cards.put("R", r.attributes)
        
        VisualBasic visual = new VisualBasic()
        cards.put("Visual Basic", visual.attributes)
        
        D d = new D()
        cards.put("D", d.attributes)
        
        MATLAB matlab = new MATLAB()
        cards.put("MATLAB", matlab.attributes)
        
        Go go = new Go()
        cards.put("Go", go.attributes)
        
        Perl perl = new Perl()
        cards.put("Perl", perl.attributes)
        
        SAS sas = new SAS()
        cards.put("SAS", sas.attributes)
        
        PLSQL pl = new PLSQL()
        cards.put("PL/SQL", pl.attributes)
         
        Dart dart = new Dart()
        cards.put("Dart", dart.attributes)   
        
        Rust rust = new Rust()
        cards.put("Rust", rust.attributes)
        
        Scratch scratch = new Scratch()
        cards.put("Scratch", scratch.attributes)
        
        Lisp lisp = new Lisp()
        cards.put("Lisp", lisp.attributes)
        
        COBOL cobol = new COBOL()
        cards.put("COBOL", cobol.attributes)
        
        Fortran fortran = new Fortran()
        cards.put("Fortran", fortran.attributes)
        
        Scala scala = new Scala()
        cards.put("Scala", scala.attributes)
        
        RPG rpg = new RPG()
        cards.put("RPG", rpg.attributes)
        
        TransactSQL transact = new TransactSQL()
        cards.put("Transact-SQL", transact.attributes)
        
        setCards()
    }
    
    def setCards(){
        cards = cards.sort{a,b -> a.key <=> b.key}
        playerCards = cards.take(16)
        computerCards = cards.drop(16)
    }
}

class Java extends Card{
    Java(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))
        attributes.put(" ", " ")    
    }
}

class C extends Card{
    C(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class Python extends Card{
    Python(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class CPlus extends Card{
    CPlus(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class CSharp extends Card{
    CSharp(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class DotNet extends Card{
    DotNet(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class JavaScript extends Card{
    JavaScript(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class PHP extends Card{
    PHP(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class SQL extends Card{
   SQL(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class Swift extends Card{
    Swift(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class Ruby extends Card{
    Ruby(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class ObjectiveC extends Card{
    ObjectiveC(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class Delphi extends Card{
    Delphi(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class Groovy extends Card{
    Groovy(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class Assembly extends Card{
    Assembly(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class R extends Card{
    R(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class VisualBasic extends Card{
    VisualBasic(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class D extends Card{
    D(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class MATLAB extends Card{
    MATLAB(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class Go extends Card{
    Go(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class Perl extends Card{
    Perl(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class SAS extends Card{
    SAS(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class PLSQL extends Card{
    PLSQL(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class Dart extends Card{
    Dart(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class Rust extends Card{
    Rust(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class Scratch extends Card{
    Scratch(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class Lisp extends Card{
    Lisp(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class COBOL extends Card{
    COBOL(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class Fortran extends Card{
    Fortran(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class Scala extends Card{
    Scala(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class RPG extends Card{
    RPG(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

class TransactSQL extends Card{
    TransactSQL(){
        for(int i = 0; i < 5; i++){
            x = Math.abs(new Random().nextInt() % (10 - 0))
            list.add(x)
        }
        setAttributes()
    }
    
    def setAttributes(){
        attributes.put("Confiabilidade", list.get(0))
        attributes.put("Aprendizado", list.get(1))
        attributes.put("Eficiencia", list.get(2))
        attributes.put("Portabilidade", list.get(3))
        attributes.put("Reusabilidade", list.get(4))    
    }
}

abstract class Card {
    def attributes = [:] 
    def x = 0
    def list = []
       
    abstract def setAttributes()
}