import sys
import os
import csv
import review_pb2 as review
from google.protobuf.internal import encoder
from review.Review import *
from review.Data import *
import flatbuffers

def read_record_as_fbs(builder, row):
   if (len(row)>=16):
       r0 = builder.CreateString(row[0])
       if (row[2]):
           r2 = builder.CreateString(row[2])
       r3 = builder.CreateString(row[3])
       r4 = builder.CreateString(row[4])
       r5 = builder.CreateString(row[5])
       r7 = builder.CreateString(row[7])
       if (row[10]):
            r10 = builder.CreateString(row[10])
       if (row[11]):
            r11 = builder.CreateString(row[11])
       if (row[12]):
            r12 = builder.CreateString(row[12])
       if (row[14]):
            r14 = builder.CreateString(row[14])
       if (row[15]):
            r15 = builder.CreateString(row[15])
       ReviewStart(builder)
       ReviewAddPackageName(builder, r0)
       if (row[1]):
          ReviewAddAppVersionCode(builder, int(row[1], 10))
       if (row[2]):
          ReviewAddAppVersionName(builder, r2)
       ReviewAddReviewerLanguage(builder, r3)
       ReviewAddDevice(builder, r4)
       ReviewAddReviewSubmitDateAndTime(builder, r5)
       ReviewAddReviewSubmitMillisSinceEpoch(builder, int(row[6], 10))
       ReviewAddReviewLastUpdateDateAndTime(builder, r7)
       ReviewAddReviewLastUpdateMillisSinceEpoch(builder, int(row[8], 10))
       ReviewAddReviewStarRating(builder, int(row[9]))
       if (row[10]):
           ReviewAddReviewTitle(builder, r10)
       if (row[11]):
           ReviewAddReviewText(builder, r11)
       if (row[12]):
           ReviewAddDeveloperReplyDataAndTime(builder, r12)
       if (row[13]):
           try:
               ReviewAddDeveloperReplyMillisSinceEpoch(builder, int(row[13]))
           except:
                a = 1
       if (row[14]):
           ReviewAddDeveloperReplyText(builder, r14)
       if (row[15]):
           ReviewAddReviewLink(builder, r15)
       return ReviewEnd(builder)

def read_record_as_pb(row):
   if (len(row)>=16):
       rev = review.Review()
       rev.package_name = row[0]
       if (row[1]):
          rev.app_version_code = int(row[1], 10)
       if (row[2]):
          rev.app_version_name = row[2]
       rev.reviewer_language = row[3]
       rev.device = row[4]
       rev.review_submit_date_and_time = row[5]
       rev.review_submit_millis_since_epoch = int(row[6], 10)
       rev.review_last_update_date_and_time = row[7]
       rev.review_last_update_millis_since_epoch = int(row[8])
       rev.review_star_rating = int(row[9])
       if (row[10]):
          rev.review_title = row[10]
       if (row[11]):
          rev.review_text = row[11]
       if (row[12]):
          rev.developer_reply_data_and_time = row[12]
       if (row[13]):
           try:
                rev.developer_reply_millis_since_epoch = int(row[13])
           except:
                a = 1
       if (row[14]):
          rev.developer_reply_text = row[14]
       if (row[15]):
          rev.review_link = row[15]
       return rev

def read_csv(input_filename, output_filename):
    with open(input_filename, 'r') as f:
       has_header = csv.Sniffer().sniff(f.read(1024))
       f.seek(0)
       csv_in = csv.reader((line.replace('\0','').replace('\r','') for line in f), delimiter=",")
       if has_header:
         headers = next(csv_in)
         output_basename, output_extension = os.path.splitext(output_filename)
         out_file = open(output_filename, "a")
         if (output_extension == ".fbs"):
               buf = write_fbs(out_file, csv_in)
         if (output_extension == ".pb"):
               write_pb(out_file, csv_in)
         out_file.close()

def read_fbs(input_filename, output_filename):
    with open(input_filename, 'rb') as f:
       buf = f.read()
       buf = bytearray(buf)
       data = Data.GetRootAsData(buf, 0)
       n = data.ReviewLength()
       print n
       for i in xrange(n):
           reviews = data.Review(i)
#           if reviews:
#               print "%s,%d" % (reviews.PackageName(), reviews.AppVersionCode())

def write_fbs(f, csv_in):
       builder = flatbuffers.Builder(0)
       n = 0
       vec = []
       for row in csv_in:
           rev = read_record_as_fbs(builder, row)
           if rev:
               vec.append(rev)
               n = n + 1
       vector = DataStartReviewVector(builder, 0)
       for i in xrange(n):
           if vec[i]:
               builder.PrependUOffsetTRelative(vec[i])
       vector = builder.EndVector(n)
       DataStart(builder)
       DataAddReview(builder, vector)
       data = DataEnd(builder)
       builder.Finish(data)
       gen_buf, gen_off = builder.Bytes, builder.Head()
       f.write(gen_buf[gen_off:])

def write_pb(f, csv_in):
       for row in csv_in:
           rev = read_record_as_pb(row)
           if rev:
               serializedMessage = rev.SerializeToString()
               delimiter = encoder._VarintBytes(len(serializedMessage))
               f.write(delimiter + serializedMessage)

if __name__ == "__main__":
       if (os.path.exists(sys.argv[2])):
           os.remove(sys.argv[2])
       input_filename, input_extension = os.path.splitext(sys.argv[1])
       if (input_extension == ".csv"):
           read_csv(sys.argv[1], sys.argv[2])
       if (input_extension == ".fbs"):
           read_fbs(sys.argv[1], sys.argv[2])

