function getRandomInt(max) {
    return Math.floor(Math.random() * max);
}

// Fisher-Yates
function shuffle(array) {
    for (var i = array.length - 1; i > 0; i--) {
        var j = getRandomInt(i + 1);
        var temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    return array;
}

function generateSecretNumber() {
    var digits = '0123456789'.split('');
    
    var firstDigitIndex = getRandomInt(9) + 1;
    var firstDigit = digits.splice(firstDigitIndex, 1)[0];

    shuffle(digits);
    
    var remainingDigits = digits.slice(0, 3);

    return [firstDigit].concat(remainingDigits).join('');
}

function checkGuess(secret, guess) {
    var bulls = 0;
    var cows = 0;

    for (var i = 0; i < guess.length; i++) {
        if (guess[i] === secret[i]) {
            bulls++;
        } else if (secret.indexOf(guess[i]) !== -1) {
            cows++;
        }
    }

    return {bulls: bulls, cows: cows};
}

function getHint(secret, query) {
    switch (query) {
        case "Первая":
            return secret.charAt(0);
        case "Вторая":
            return secret.charAt(1);
        case "Третья":
            return secret.charAt(2);
        case "Четвертая":
            return secret.charAt(3);
    }
}