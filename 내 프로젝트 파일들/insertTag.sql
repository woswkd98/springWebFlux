DELIMITER $$

USE `bidding`$$

DROP PROCEDURE IF EXISTS `insertTag`$$

CREATE DEFINER=`scott`@`localhost` PROCEDURE `insertTag`(
	IN inContext VARCHAR(20),
	IN requestId INT
    )
BEGIN
	DECLARE id INT DEFAULT 0;
	
	SELECT tagId INTO id FROM tag WHERE CONTEXT = inContext; #일단 태그의 내용과 맞는 TAG가 있는지 확인한다
	
	IF id <= 0 THEN
		INSERT INTO tag(CONTEXT) VALUES (inContext); # 만약 없으면 생성시켜서 집어넣고
		SET id = LAST_INSERT_ID(); #인덱스를 가져온다 
	ELSE 
		UPDATE tag SET requestCount = requestCount + 1 WHERE tagId = id; #태그의 내용과 맞는 TAG가 있으면 카운트 증가
	END IF;
	
	INSERT INTO request_has_Tag(request_requestId, tag_tagId) VALUES (requestId, id); #마지막은 다 대 다 관계 저장
END$$

DELIMITER ;