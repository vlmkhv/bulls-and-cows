function getRandomInt(max) {
    return Math.floor(Math.random() * max);
}

// Fisher-Yates
function shuffle(array) {
    var currentIndex = array.length;
    while (currentIndex > 0) {
        var randomIndex = getRandomInt(currentIndex);
        currentIndex--;
        var temp = array[currentIndex];
        array[currentIndex] = array[randomIndex];
        array[randomIndex] = temp;
    }
    return array;
}

function generateSecretNumber() {
    var digits = [];
    for (var i = 0; i < 10; i++) {
        digits.push(i);
    }

    var firstDigitIndex = getRandomInt(9) + 1;
    var firstDigit = digits.splice(firstDigitIndex, 1)[0];

    shuffle(digits);
    var remainingDigits = digits.slice(0, 3);

    return [firstDigit].concat(remainingDigits).join('');
}

function checkGuess(secret, guess) {
    var bulls = 0;
    var cows = 0;

    var secretDigits = {};
    for (var i = 0; i < secret.length; i++) {
        secretDigits[secret[i]] = true;
    }

    for (var i = 0; i < guess.length; i++) {
        if (guess[i] === secret[i]) {
            bulls++;
        } else if (secretDigits[guess[i]]) {
            cows++;
        }
    }

    return {bulls: bulls, cows: cows};
}

function checkUnique(guess) {
    var digits = {};
    for (var i = 0; i < guess.length; i++) {
        if (digits[guess[i]]) {
            return false;
        }
        digits[guess[i]] = true;
    }
    return true;
}
