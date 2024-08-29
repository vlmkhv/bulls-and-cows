require: bullsAndCows.js

theme: /

    state: Start
        q!: $regex</start>
        script:
            $session.secret = generateSecretNumber();
            $session.turns = 0;
            $session.hintsUsed = 0;
        a: Игра «Быки и коровы». Я загадал четырёхзначное число с неповторяющимися цифрами. Число не может начинаться с 0.\nПопробуй угадать!
        buttons:
            "Правила" -> /Rules
            "Взять подсказку" -> /HintMenu
            "Сдаться" -> /GiveUp

    state: Rules
        a: Попробуй угадать число, а я сообщу в ответ, сколько цифр угадано без совпадения с их позициями (количество коров) и сколько угадано вплоть до позиции в тайном числе (количество быков).\n
           *Например*, если я задумал число «3219», то попытка «2310» даст результат: две «коровы» (две цифры: «2» и «3» — угаданы на неверных позициях) и один «бык» (одна цифра «1» угадана вплоть до позиции).
        buttons:
            "Сдаться" -> /GiveUp
            "Взять подсказку" -> /HintMenu
    
    state: Guess
        
        q: $regex<^[1-9][0-9]{0,}$>
        
        script:
            var guess = $request.query;
            var errorMessage = null;
            
            if (guess.length < 4) {
                errorMessage = "Число слишком короткое!";
            } else if (guess.length > 4) {
                errorMessage = "Число слишком длинное!";
            } else if (_.uniq(guess).join('') !== guess) {
                errorMessage = "Цифры повторяются!";
            }
            
            if (errorMessage) {
                $reactions.answer(errorMessage);
                $reactions.transition("/NoMatch");
            } else {
                
                var res = checkGuess($session.secret, guess);
                
                $session.turns += 1;
                
                if (res.bulls === 4) {
                    var msg = "Ты угадал за " + $session.turns + " " + $nlp.conform("ход", $session.turns) + "!\n"
                    msg += "Использовано подсказок: " + $session.hintsUsed
                    $reactions.answer(msg);
                    $reactions.transition("/Start");
                } else {
                    $reactions.answer(res.bulls + " " + $nlp.conform("бык", res.bulls) + ", " + res.cows + " " + $nlp.conform("корова", res.cows));
                }
            }
        buttons:
            "Правила" -> /Rules
            "Взять подсказку" -> /HintMenu
            "Сдаться" -> /GiveUp    

    state: GiveUp
        a: Ну что ж, начнём заново! Было загадано число {{$session.secret}}
        go!: /Start
        
    state: HintMenu
        a: Выбери подсказку
        buttons:
            "Первая цифра" -> /GetHint
            "Вторая цифра" -> /GetHint
            "Третья цифра" -> /GetHint
            "Четвертая цифра" -> /GetHint
            "Вернуться" -> /
    
    state: GetHint
        q: * цифра || fromState = "HintMenu"
        script:
            var query = $request.query.split(' ')[0];
            $temp.digit = getHint($session.secret, query);
            $session.hintsUsed += 1;
        a: {{$request.query}}: {{$temp.digit}}

    state: NoMatch || noContext = true
        event!: noMatch
        a: Пожалуйста, введи четырёхзначное число с неповторяющимися цифрами. Число не может начинаться с 0.