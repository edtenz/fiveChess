实现人机对弈的五子棋
=========

Man-machine five chess game written with JAVA 

##1. 用贪心算法来实现的“智能”
对机器方来说，每次依据棋局用一个评价函数来评估各个落子点位置的值，根据值选出“最优”的落子位置，是本项目实现人机对弈的核心，具体设计思想见：[设计文档](https://github.com/EdgarTeng/fiveChess/blob/master/docs/%E7%AE%80%E5%8D%95%E4%BA%BA%E6%9C%BA%E5%AF%B9%E5%BC%88-%E8%AE%BE%E8%AE%A1%E6%80%9D%E6%83%B3%E4%B9%8B%E8%AF%84%E4%BB%B7%E5%87%BD%E6%95%B0%E7%9A%84%E9%80%89%E5%8F%96.pdf)

##2. 代码结构
采用面向对象的思想，本工程的类分为：
* 棋盘类(ChessBoard),画出整个棋盘，作为棋子的容器，侦听鼠标点击事件，还有判赢系统也在这个类里面；
* 棋子类(Chessman),画出棋子；
* 机器类(Machine),获取了棋局的信息之后，机器类做出决策选择下子；
* 位置类(Location)，棋子的位置。



