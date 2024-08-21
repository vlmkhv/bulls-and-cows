require: bullsAndCows.js

theme: /

    state: Start
        q!: $regex</start>
        script:
            $session.secret = generateSecretNumber();
        a: Игра "Быки и коровы". Я загадал четырёхзначное число с неповторяющимися цифрами. Попробуй угадать!
        
    state: Guess
        q!: $regex<^[1-9][0-9]{3}$>
        script:
            var guess = $request.query;
            if (checkUnique(guess)) {
                var res = checkGuess($session.secret, guess);
                if (res.bulls === 4) {
                    $reactions.answer("Ты угадал!");
                    $reactions.transition("/Start");
                } else {
                    //$reactions.answer($session.secret);
                    $reactions.answer("Быки: " + res.bulls + ", коровы: " + res.cows);
                }
            }
            else {
                $reactions.transition("/NoMatch");
            }
            

    state: NoMatch
        event!: noMatch
        a: Пожалуйста, введи четырёхзначное число с неповторяющимися цифрами.