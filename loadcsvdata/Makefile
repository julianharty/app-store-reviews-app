INPUT=../ExampleReviews/kiwix/reviews_org.kiwix.kiwixmobile_201606.csv
OUTPUT=result.pb result.fbs

all: $(OUTPUT)

result.pb: read.py $(INPUT)
	/usr/bin/time python $^ result.pb pb

result.fbs: read.py $(INPUT)
	/usr/bin/time python $^ result.fbs fbs

read.py: review_pb2.py Review.py
	touch $@

review.fbs: review.proto
	flatc --proto review.proto

Review.py: review.fbs
	flatc -p -o . review.fbs
	mv review/Review.py .

review_pb2.py: review.proto
	protoc -I=. --python_out=. review.proto

clean::
	rm -rf $(OUTPUT)
