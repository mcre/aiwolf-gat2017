チームm_cre (プロトコル部門)
====

[人狼知能プレ大会@GAT2017](http://aiwolf.org/event-2/gat2017) プロトコル部門の出場エージェントです。

## プレイヤーjarファイル

* [McrePlayer.jar](/McrePlayer.jar)
  + クラスは ```net.mchs_u.mc.aiwolf.curry.McrePlayer``` を指定してください。

## クラス説明

* net.mchs_u.mc.aiwolf.baikin04.McrePlayer
  + 第二回人狼知能大会出場エージェントを、人狼知能プラットフォーム0.4.4で最低限うごくようにしたもの
* net.mchs_u.mc.aiwolf.curry.McrePlayer
  + 今回の出場エージェント
* net.mchs_u.mc.aiwolf.curry_snapshot.McrePlayer
  + curryに変更を加える場合に変更前後の勝率変化を確認するためにある時点の状態を保存しておいたもの

## プログラム解説

第2回大会の提出エージェントをベースにしています。
第2回大会のエージェントの解説は[こちら](http://aiwolf.org/control-panel/wp-content/uploads/2016/08/m_cre_CEDEC2016.pdf)をご覧ください。
第2回大会からの変更点は以下のとおりです。

* ソースコードの整理
  + パラメータ探索アルゴリズムを消去。読みやすくなっていると思います。
* 第2回大会で未実装だった「調整パラメータ」(※第2回資料参照)を実装
* 人狼知能プラットフォーム0.4.4に対応
* 任意の人数で動作するようにした
  + 第2回大会では15人でしか動作しなかった
* 狩人のとき、襲撃失敗した場合に護衛先が人狼であると推定する確率を下げるパターンを追加
* 占い先・護衛先・襲撃先から、直前の追放者を除外するように変更
* パワープレイを実装
  + 狂人CO ⇒ 人狼CO ⇒ 人狼のうち誰かがREQUEST(VOTE AGENT[XX]) ⇒ 狂人と人狼がそれに従う
* 再投票を実装
  + 1回目投票で最高票数だった中から投票優先度が一番高い対象に投票

## 連絡先

* [twitter: @m_cre](https://twitter.com/m_cre)
* [blog](http://www.mchs-u.net)

## License

* MIT
  + see LICENSE