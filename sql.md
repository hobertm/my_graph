
```

WITH "file:///home/hb/Documents/data/merge/merge.json" AS url
CALL apoc.load.json(url) YIELD value
UNWIND value.items AS c
MERGE (company:Company {id:c._id}) ON CREATE
  SET company.name = c.name, company.url = c.url, company.Address = c.Address ,
  company.RegisterStatus = c.BasicInfo.RegisterStatus,
  company.RegistrationAuthority = c.BasicInfo.RegistrationAuthority,
  company.LegalRepresentative = c.BasicInfo.LegalRepresentative,
  company.BusinessDate = c.BasicInfo.BusinessDate,
  company.CourtNotice = [notice in [c.JudicialRisk.CourtNotice] | apoc.convert.toString(notice)],
  company.LegalProceedings = [lp in [c.JudicialRisk.LegalProceedings] | apoc.convert.toString(lp)]

FOREACH (keyperson IN c.BasicInfo.KeyPersonInfo | 
  MERGE (person:Person {name:keyperson.Name , JobName:keyperson.JobName}) MERGE (person)-[:KEY_PERSON]->(company))
;

```


```清空
MATCH (n) DETACH DELETE n
```

```stackoverflow创建
WITH "https://api.stackexchange.com/2.2/questions?pagesize=100&order=desc&sort=creation&tagged=neo4j&site=stackoverflow&filter=!5-i6Zw8Y)4W7vpy91PMYsKM-k9yzEsSC1_Uxlf" AS url
CALL apoc.load.json(url) YIELD value
UNWIND value.items AS q
MERGE (question:Question {id:q.question_id}) ON CREATE
  SET question.title = q.title, question.share_link = q.share_link, question.favorite_count = q.favorite_count

FOREACH (tagName IN q.tags | MERGE (tag:Tag {name:tagName}) MERGE (question)-[:TAGGED]->(tag))
FOREACH (a IN q.answers |
   MERGE (question)<-[:ANSWERS]-(answer:Answer {id:a.answer_id})
   MERGE (answerer:User {id:a.owner.user_id}) ON CREATE SET answerer.display_name = a.owner.display_name
   MERGE (answer)<-[:PROVIDED]-(answerer)
)
WITH * WHERE NOT q.owner.user_id IS NULL
MERGE (owner:User {id:q.owner.user_id}) ON CREATE SET owner.display_name = q.owner.display_name
MERGE (owner)-[:ASKED]->(question)
```
