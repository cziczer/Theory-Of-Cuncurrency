// Teoria Współbieżnośi, implementacja problemu 5 filozofów w node.js
// Opis problemu: http://en.wikipedia.org/wiki/Dining_philosophers_problem
//   https://pl.wikipedia.org/wiki/Problem_ucztuj%C4%85cych_filozof%C3%B3w
// DONE 1. Dokończ implementację funkcji podnoszenia widelca (Fork.acquire).
// DONE 2. Zaimplementuj "naiwny" algorytm (każdy filozof podnosi najpierw lewy, potem
//    prawy widelec, itd.).
// DONE 3. Zaimplementuj rozwiązanie asymetryczne: filozofowie z nieparzystym numerem
//    najpierw podnoszą widelec lewy, z parzystym -- prawy. 
// DONE 4. Zaimplementuj rozwiązanie z kelnerem (według polskiej wersji strony)
// DONE 5. Zaimplementuj rozwiążanie z jednoczesnym podnoszeniem widelców:
//    filozof albo podnosi jednocześnie oba widelce, albo żadnego.
// 6. Uruchom eksperymenty dla różnej liczby filozofów i dla każdego wariantu
//    implementacji zmierz średni czas oczekiwania każdego filozofa na dostęp 
//    do widelców. Wyniki przedstaw na wykresach.

var Fork = function() {
    this.state = 0;
    return this;
}

Fork.prototype.acquire = async function(cb) { 
    // zaimplementuj funkcję acquire, tak by korzystala z algorytmu BEB
    // (http://pl.wikipedia.org/wiki/Binary_Exponential_Backoff), tzn:
    // 1. przed pierwszą próbą podniesienia widelca Filozof odczekuje 1ms
    // 2. gdy próba jest nieudana, zwiększa czas oczekiwania dwukrotnie
    //    i ponawia próbę, itd.
    let time = 1;
    await new Promise(_ => {setTimeout(_, time)});

    while (this.state != 0) {
        time *= 2;
        await new Promise(_ => {setTimeout(_, time)});
    }
    this.state = 1;
}

Fork.prototype.release = function() { 
    this.state = 0; 
}

var Philosopher = function(id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id+1) % forks.length;
    return this;
}

eat = async function () {
    await new Promise(_ => {setTimeout(_, 1)});
}

Philosopher.prototype.startNaive = function(count) {
    var forks = this.forks,
    f1 = this.f1,
    f2 = this.f2

    // zaimplementuj rozwiązanie naiwne
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców   

    return new Promise(async resolve => {
        const timer = []
        for (var i=0; i<count; i++) {
            let startTime = new Date().getTime();

            await forks[f1].acquire();
            await forks[f2].acquire();

            timer.push(new Date().getTime() - startTime)

            eat();

            forks[f1].release();
            forks[f2].release();
        }
        resolve((timer.reduce((a, b) => a + b, 0)) / count);
    });

}

Philosopher.prototype.startAsym = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id

    // zaimplementuj rozwiązanie asymetryczne
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców
    return new Promise(async resolve => {
        const timer = []
        for (var i=0; i<count; i++) {
            let startTime = new Date().getTime();
            if (id % 2 == 0) {
                await forks[f2].acquire();
                await forks[f1].acquire();
            }
            else {
                await forks[f1].acquire();
                await forks[f2].acquire();
            }

            timer.push(new Date().getTime() - startTime)

            eat();

            forks[f1].release();
            forks[f2].release();
        }
        resolve((timer.reduce((a, b) => a + b, 0)) / count);
    });
}


// KELNER
var Conductor = function(forks) {
    this.forks = forks - 1;
    return this;
}

Conductor.prototype.acquire = async function(cb) {
    let time = 1;
    await new Promise(_ => {setTimeout(_, time)});
    
    while (this.forks == 0) {
        time *= 2;
        await new Promise(_ => {setTimeout(_, time)});
    }
    this.forks -= 1;
}

Conductor.prototype.release = function() {
    this.forks += 1;
}

Philosopher.prototype.startConductor = function(count, conductor) {
    var forks = this.forks,
    f1 = this.f1,
    f2 = this.f2
    
    // zaimplementuj rozwiązanie z kelnerem
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców
    

    return new Promise(async resolve => {
        const timer = []
        for (var i=0; i<count; i++) {
            const startTime = new Date().getTime();
            await conductor.acquire();
            await forks[f1].acquire();
            await forks[f2].acquire();
            timer.push(new Date().getTime() - startTime);
            eat();

            forks[f1].release();
            forks[f2].release();
            conductor.release();
        }
        resolve((timer.reduce((a, b) => a + b, 0)) / count);
    });
}


// TODO: wersja z jednoczesnym podnoszeniem widelców
// Algorytm BEB powinien obejmować podnoszenie obu widelców, 
// a nie każdego z osobna

async function takeBothForks(forks, f1, f2) {
    let time = 1;
    await new Promise(_ => {setTimeout(_, time)});

    while (forks[f1].state != 0 || forks[f2].state != 0) {
        time *= 2;
        await new Promise(_ => {setTimeout(_, time)});
    }
    forks[f1].state = 1;
    forks[f2].state = 1;
}

Philosopher.prototype.pickBothForksApproach = function(count) {
    var forks = this.forks,
    f1 = this.f1,
    f2 = this.f2


    return new Promise(async resolve => {
        const timer = []
        for (var i=0; i<count; i++) {
            const startTime = new Date().getTime();
            await takeBothForks(forks, f1, f2);
            timer.push(new Date().getTime() - startTime);

            eat();

            forks[f1].release();
            forks[f2].release();
        }
        resolve((timer.reduce((a, b) => a + b, 0)) / count);
    });
}



//deadlock
// for (var i = 0; i < N; i++) {
//     philosophers[i].startNaive(meals).then(resolve => console.log(resolve));
// }

asymAproach = async (meals, philosophers_count) => {
    return new Promise(async resolve => {
        const timer = []
        for (var i = 0; i < philosophers_count; i++) {
            await philosophers[i].startAsym(meals).then(resolve => timer.push(resolve));
        }
        resolve((timer.reduce((a, b) => a + b, 0)) / N);
    })
}

const conductor = new Conductor(N);
conductorAproach = async (meals, philosophers_count) => {
    return new Promise(async resolve => {
        const timer = []
        for (var i = 0; i < philosophers_count; i++) {
            await philosophers[i].startConductor(meals, conductor).then(resolve => timer.push(resolve));
        }
        resolve((timer.reduce((a, b) => a + b, 0)) / N);
    })
}

botchForksAproach = async (meals, philosophers_count) => {
    return new Promise(async resolve => {
        const timer = []
        for (var i = 0; i < philosophers_count; i++) {
            await philosophers[i].pickBothForksApproach(meals).then(resolve => timer.push(resolve));
        }
        resolve((timer.reduce((a, b) => a + b, 0)) / N);
    })
}

const test = async (meals, philosophers_count) => {
    await asymAproach(meals, philosophers_count).then(resolve => console.log("Asym average time " + resolve + " ms"))
    await conductorAproach(meals, philosophers_count).then(resolve => console.log("Conductor average time " + resolve + " ms"))
    await botchForksAproach(meals, philosophers_count).then(resolve => console.log("2 forks average time " + resolve + " ms"))
} 

var N = 5
var forks = [];
var philosophers = []
for (var i = 0; i < N; i++) {
    forks.push(new Fork());
}

for (var i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks));
}

test(5, N)