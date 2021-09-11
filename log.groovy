#!/usr/bin/env groovy
/**
 * Black        0;30     Dark Gray     1;30
 * Red          0;31     Light Red     1;31
 * Green        0;32     Light Green   1;32
 * Brown/Orange 0;33     Yellow        1;33
 * Blue         0;34     Light Blue    1;34
 * Purple       0;35     Light Purple  1;35
 * Cyan         0;36     Light Cyan    1;36
 * Light Gray   0;37     White         1;37
 *
 *
 * @return
 */
def loadColors(){
    RED='\033[0;31m'
    LIGHT_RED='\033[1;31m'
    BLUE='\033[0;34m'
    LIGHT_BLUE='\033[1;34m'
    CYAN='\033[0;36m'
    LIGHT_CYAN='\033[1;36m'
    GREEN='\033[0;32m'
    LIGHT_GREEN='\033[1;32m'
    PURPLE='\033[0;35m'
    LIGHT_PURPLE='\033[1;35m'
    BROWN='\033[0;33m'
    ON_YELLOW='\033[0;43m'
    GRAY='\033[0;30m'
    LIGHT_GRAY='\033[0;37m'
    NC='\033[0m'

}


def info(message) {
    ansiColor('xterm') {
        loadColors()
        echo "${GREEN} [INFO] ${message} ${NC} "
    }
}

def warning(message) {
    ansiColor('xterm') {
        loadColors()
        echo "${ON_YELLOW} [WARNING] ${message} ${NC} "
    }
}

def error(message) {
    ansiColor('xterm') {
        loadColors()
        echo "${RED} [ERROR] ${message} ${NC} "
    }
}

def success(message) {
    ansiColor('xterm') {
        loadColors()
        echo "${BLUE} [SUCCESS] ${message} ${NC} "
    }
}

def debug(message){
    ansiColor('xterm') {
        loadColors()
        echo "${PURPLE} [DEBUG] ${message} ${NC} "
    }
}

def block() {
    echo "========== ========== ========== ========== ========== =========="
}
