INPUT=../ExampleReviews/kiwix/reviews_org.kiwix.kiwixmobile_201606.csv
OUTPUT=result.pb result.fbs result.txt

all: $(OUTPUT)

result.txt: read.py result.fbs
	/usr/bin/time python $^ read.txt 

result.pb: read.py $(INPUT)
	/usr/bin/time python $^ result.pb 

result.fbs: read.py $(INPUT)
	/usr/bin/time python $^ result.fbs

read.py: review_pb2.py review/Data.py
	touch $@

review.fbs: review.proto
	flatc --proto review.proto

review/Data.py review/Review.py: review.fbs
	flatc -p -o . review.fbs

review_pb2.py: review.proto
	protoc -I=. --python_out=. review.proto

clean::
	rm -rf $(OUTPUT)
