<?php 

$_DataMap = array(
	'Customer' => array(
		'id' => 'id',
		'sid' => 'sid',
		'name' => 'name',
		'sign' => 'sign',
		'face' => 'face',
		'faceurl' => 'faceurl',
		'blogcount' => 'blogcount',
		'fanscount' => 'fanscount',
		'uptime' => 'uptime',
	),
	'Blog' => array(
		'id' => 'id',
		'face' => 'face',
		'author' => 'author',
		'content' => 'content',
		'comment' => 'comment',
		'uptime' => 'uptime',
	),
	'Comment' => array(
		'id' => 'id',
		'content' => 'content',
		'uptime' => 'uptime',
	),
	'Image' => array(
		'id' => 'id',
		'url' => 'url',
		'type' => 'type',
	),
	'Notice' => array(
		'id' => 'id',
		'type' => 'type',
		'msg' => 'msg',
		'createtime' => 'createtime',
	),
    'User' => array(
		'sid' => 'sid',
		'uid' => 'uid',
        'nickname' => 'nickname',
        'age' => 'age',
        'sex' => 'sex',
		'city' => 'city',
		'regtime' => 'regtime',
        'lastlogintime' => 'lastlogintime',
        'heartsay' => 'heartsay',
        'faceurl' => 'faceurl',
	),
	'UserMe' => array(
		'sid' => 'sid',
		'uid' => 'uid',
        'nickname' => 'nickname',
		'money' => 'money',
		'regtime' => 'regtime',
		'vipendtime' => 'vipendtime',
		'age' => 'age',
        'sex' => 'sex',
		'stature' => 'stature',
		'weight' => 'weight',
		'bodyshape' => 'bodyshape',
		'ismarried' => 'ismarried',
		'degree' => 'degree',
		'ocupation' => 'ocupation',
		'city' => 'city',
		'qq' => 'qq',
		'tel' => 'tel',
		'email' => 'email',
		'attitude' => 'attitude',
		'experience' => 'experience',
		'introduction' => 'introduction',
        'lastlogintime' => 'lastlogintime',
        'heartsay' => 'heartsay',
        'faceurl' => 'faceurl',
	),
	
	'UserYou' => array(
		'sid' => 'sid',
		'uid' => 'uid',
        'nickname' => 'nickname',
		'regtime' => 'regtime',
		'age' => 'age',
        'sex' => 'sex',
		'stature' => 'stature',
		'weight' => 'weight',
		'bodyshape' => 'bodyshape',
		'ismarried' => 'ismarried',
		'degree' => 'degree',
		'ocupation' => 'ocupation',
		'city' => 'city',
		'qq' => 'qq',
		'tel' => 'tel',
		'email' => 'email',
		'attitude' => 'attitude',
		'experience' => 'experience',
		'introduction' => 'introduction',
        'lastlogintime' => 'lastlogintime',
        'heartsay' => 'heartsay',
        'faceurl' => 'faceurl',
	),

	'Share' => array(
		'id' => 'id',
        'uid' => 'uid',
        'title' => 'title',
        'type' => 'type',
        'click' => 'click',
        'createtime' => 'createtime',
        'content' => 'content',
		'author' => 'author',
		'faceurl' => 'faceurl',
	),
	'Square' => array(
		'id' => 'id',
        'uid' => 'uid',
		'authorsex' => 'authorsex',
        'click' => 'click',
        'createtime' => 'createtime',
        'content' => 'content',
		'author' => 'author',
		'authorface' => 'faceurl',
    ),
	'Inform' => array(
		'id' => 'id',
        'type' => 'type',
        'informerid' => 'informerid',
        'informedid' => 'informedid',
		'informtime' => 'informtime',
    ),

);

function M ($model, $data)
{
	global $_DataMap;
	
	$dataMap = isset($_DataMap[$model]) ? $_DataMap[$model] : null;
	if ($dataMap) {
		$dataRes = array();
		foreach ((array) $data as $k => $v) {
			if (array_key_exists($k, $dataMap)) {
				$mapKey = $dataMap[$k];
				$dataRes[$mapKey] = $v;
			}
		}
		return $dataRes;
	}
	
	return $data;
}
