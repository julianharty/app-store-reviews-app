INPUT=../ExampleReviews/kiwix/reviews_org.kiwix.kiwixmobile_201606.csv
OUTPUT=result.pb

$(OUTPUT): read.py $(INPUT)
	python $^ $(OUTPUT)

read.py: review_pb2.py
	touch $@

review_pb2.py: review.proto
	protoc -I=. --python_out=. review.proto

clean::
	rm -rf mylmdb
