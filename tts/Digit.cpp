#include "stdafx.h"
#include "digit.h"

char ttt[500]="點";  //處理小數點
char *digitunit[];
static char digit[]={"零一二三四五六七八九"};

static char *am[]={"上午",  "早上","早晨","凌晨","上午","半夜","清晨","ＡＭ","Ａ．Ｍ","ＡＭ．","Ａ．Ｍ．",NULL };
static char *pm[]={"下午",  "下午","晚上","傍晚","半夜","ＰＭ","Ｐ．Ｍ","ＰＭ．","Ｐ．Ｍ．",NULL};
static char *usd[]={"美金",  "＄","ＵＳＤ","$",NULL};
static char *ntd[]={"新台幣",  "ＮＴ","ＮＴ＄",NULL};
static char *ad[]={"西元",  "西元","公元","紀元","ＡＤ","Ａ．Ｄ","ＡＤ．","Ａ．Ｄ．",NULL};
static char *bc[]={"西元前", "西元前","公元前","紀元前","ＢＣ","Ｂ．Ｃ","ＢＣ．","Ｂ．Ｃ．",NULL};

/************************************************************************/
/*20071002 fable edition                                                */
/************************************************************************/
static char *percent[]={"百分之",  "百分之",NULL};

static char *g_op;

static BOOL Match(char **from,char *abbr[], char **to, BOOL bLeft)
{
	int i,len,from_len;

	from_len=strlen(g_op);

	for(i=1; abbr[i]!=NULL; i++) {
		len=strlen(abbr[i]);
		UCHAR uc=abbr[i][0];
		if(bLeft) {
			if(from_len>= len && memcmp(abbr[i],*from-len,len)==0) {
				if(uc<0xa4) {
					strcpy(*from-len,abbr[0]);
					*from -= len-strlen(abbr[0]);
				}
				return TRUE;
			}
		} else if(memcmp(*from, abbr[i], len)==0) {
			if(uc<0xa4) {
				strcpy(*to,abbr[0]);
			} else {
				strcpy(*to,abbr[i]);
			}
			*to += strlen(*to);
			*from += len;
			return TRUE;
		}
	}
	return FALSE;
}

static void One2One(char *from, char **to) {
	int j;
	for(j=0; j<(int)strlen(from); j++) {
		memcpy(*to,&digit[ (from[j]-'0')*2 ],2);
		*to += 2;
	}
}

void convert_digit(char *input,char *output)
{   
	char *to,*from;
	int len,i,j,digit_len;
	char *dp;
	char digit_tmp[500];
	char buf[300];
	
	for( i = j = 0; i < (int)strlen(input); i++) {
		UCHAR c0,c1;
		c0=input[i];
		c1=input[i+1];
		//"０","１","２","３","４","５","６","７","８","９"
		//a2af  a2b0 ...                                a2b8
		if(c0==0xa2 && c1>=0xaf && c1<=0xb8) {
			buf[j++]=c1 - 0xaf +'0';
			i++;
		}
		else if (c0 < 128 && !(c0>='0' && c0<='9'))
		{
			if(c0<0x20) c0 = 0x20 ;
			memcpy( &buf[j++], symbol[c0-0x20],2 ) ;
			j++;
		}
		else if((c0>='0' && c0<='9'))	
		{
			buf[j++]=c0;
		}/**/
		else
		{
			buf[j++]=c0;
			buf[j++]=c1;
			i++;
		}
	}
	buf[j]=0;
	
	
	dp=digit_tmp;
	g_op=to=output;
	from=buf;
	
	while(*from!=0)	{
		//next=CharNext(from);
		//next=from+1;
		if (*from<127) {
			if((*from<='9')&&(*from >='0')) {
				*to=0;
				do {
again:				
				*dp++=*from++;					
				} while ((*from<='9')&&(*from >='0')) ;
				if(*from==',' && from[1]>='0'&&from[1]<='9') {  //判斷 ,
					from++;
					goto again;
				}
				dp[0]=0;
				to[0]=0;
				j=0;
				len=strlen(to);
				digit_len=strlen(digit_tmp);
				
				int found=0;
				int more=0; //多
				/************************************************************************/
				/* 20071002 fable edition                                               */
				/************************************************************************/
/*
				if(Match(&to, cm, NULL, TRUE)) goto digit;
				if(Match(&to, m, NULL, TRUE)) goto digit;
				if(Match(&to, km, NULL, TRUE)) goto digit;
				/************************************************************************/
				/* 20071002 fable edition                                               */
				/************************************************************************/

				if(Match(&to, ad, NULL, TRUE)) goto digit;
				if(Match(&to, bc, NULL, TRUE)) goto digit;
				if(Match(&to, am, NULL, TRUE) || Match(&to, pm, NULL, TRUE)){
					to=cal_num(digit_tmp,to);
					if(memcmp(from,"點",2)==0 || memcmp(from,"：",2)==0) {
						memcpy(to,"點",2);
						from+=2;
						to+=2;
						to[0]=0;
					}
					dp=digit_tmp;
					while ((*from<='9')&&(*from >='0')){
						*dp++=*from++;
					}
					dp[0]=0;
					if(digit_tmp[0]) {
						to=cal_num(digit_tmp,to);
						if(memcmp(from,"分",2)!=0) {
							memcpy(to,"分",2);
							to+=2;
							to[0]=0;
						}
					}
					to[0]=0;
					dp=digit_tmp;
					continue;
				}
				if(memcmp(from,"：",2)==0) {
					char tmp[100];
					from+=2;
					dp[0]=0;
					strcpy(tmp,digit_tmp);
					dp=digit_tmp;
					while ((*from<='9')&&(*from >='0')){
						*dp++=*from++;
					}
					dp[0]=0;
					BOOL bMin=FALSE;
					if(memcmp(from,"分",2)==0) {
						from+=2;
						bMin=TRUE;
					} else if(!Match(&from, am, &to, FALSE)) Match(&from, pm, &to, FALSE);
					to=cal_num(tmp,to);
					memcpy(to,"點",2);
					to+=2;
					to=cal_num(digit_tmp,to);
					if(bMin || digit_tmp[0]) {
						memcpy(to,"分",2); //5:  ==>bug
						to+=2;
					}
					to[0]=0;
					dp=digit_tmp;
					continue;
				}

				if(memcmp(from,"．",2)==0) {
					char tmp[100];
					from+=2;
					dp[0]=0;
					strcpy(tmp,digit_tmp);
					dp=digit_tmp;
					while ((*from<='9')&&(*from >='0')){
						*dp++=*from++;
					}
					dp[0]=0;
					if(memcmp(from,"．",2)==0) {
						One2One(tmp, &to);
						memcpy(to,"點",2);
						to+=2;
dot_again:
						from+=2;
						One2One(digit_tmp, &to);
						memcpy(to,"點",2);
						to+=2;
						dp=digit_tmp;
						while ((*from<='9')&&(*from >='0')){
							*dp++=*from++;
						}
						if(memcmp(from,"．",2)==0) goto dot_again;
						One2One(digit_tmp, &to);
					} else {
						to=cal_num(tmp,to);
						memcpy(to,"點",2);
						to+=2;
						One2One(digit_tmp, &to);
					}
					to[0]=0;
					dp=digit_tmp;
					continue;
				}
				//NT 一定要先比對，然後才美金
				if(Match(&to, ntd, NULL, TRUE)||Match(&to, ntd, NULL, TRUE)) {
					found=-1;
					goto match_unit;
				}
				if(Match(&to, percent, NULL, TRUE)) {
//					found=1;
					goto number;
				}

match_unit:
				if(memcmp(from,"多",2)==0 || memcmp(from,"幾",2)==0) more=2;

 				for(;j>=0&& digitunit[j]!=NULL; j++) {
					if(memcmp(from+more,digitunit[j],strlen(digitunit[j]))==0) {
						if(memcmp(from,"％",2)==0) {
							memcpy(to,"百分之",6);
							to += 6;
							*to=0;
							from+=2;
						}
number:
						to=cal_num(digit_tmp,to);
						found=1;
						break;
					}
					/*
					memcpy(tpt,"個百分比",8);
					memcpy(tpt,"新台幣",6);
						memcpy(tpt,"樓",2);
						*/
				}
				UCHAR uc;
				uc=from[0];
				if(found==0) {
					if(uc<0xa4) {
						if(Match(&from, ad, &to, FALSE) || Match(&from, bc, &to, FALSE)) {
							to=cal_num(digit_tmp,to);
							memcpy(to,"年",2);
							to += 2;
						} 
			/************************************************************************/
			/* 20071002 fable edition                                               */
			/************************************************************************/
						else
						One2One(digit_tmp, &to);
			/************************************************************************/
			/* 20071002 fable edition                                               */
			/************************************************************************/
					} else {
digit:
						One2One(digit_tmp, &to);
					}
				} else if(found==-1) {
					to=cal_num(digit_tmp,to);
					memcpy(to,"元",2);
					to += 2;
				}
				to[0]=0;
				dp=digit_tmp;
			} // else BUG
			else{
				*to++=*from++;
				*to++=*from++; 
				to[0]=0;
			}
		} else{		
			*to++=*from++;
			*to++=*from++; 
			to[0]=0;
		}
	}
	*to=0;
}

	
char* cal_num(char *input,char *t)
{	
	int m,j,k,r,zc; //zero counter
	char *n,*o;
	o=t;
	zc=0;
	m=strlen(input) - 1;
	if(m<0) {
		t[0]=0;
		return t;
	}
	if(m>=16)
		{
			//Convert each digit to Chinese character
			for(k=0; k<=m; k++) {
				memcpy(o,&digit[ (input[k]-'0')*2 ],2);
				o=o+2;
			}
			*o=0;
			goto step7;
		}
	k=0;
	if(m>1) {  //5點04分： 0不可去掉
		while(input[k]=='0'){ //delete leading '0'
			if(k==m){
copy_zero:
				memcpy(o,"零",2);
				o=o+2;
				*o=0;
				goto step7;
			}
			k=k+1;
		}
	} else if(input[0]=='0' && input[1]=='0') goto copy_zero;

	m-=k;
	n=input+k;	
	k=0;
step4:
	j=(m-k)%4;
	
	if(n[k]=='0')
		{
			zc++;
			if(k==m)
			{
				goto step7;
			}
			if(j!=0 || zc>=4) 
			{
				k=k+1;
				
				if(n[k]!='0')
				{
					j=(m-k)%4;
					memcpy(o,"零",2);				
					o=o+2;
					*o=0;
					if(n[k]=='2'&&zc>=3&&j==0&&k!=m)
					{
						memcpy(o,"兩",2);				
						o=o+2;
						*o=0;
						zc=0;
						goto step6;
					}
					if(n[k]=='1'&& zc>=1 &&j==1)
					{
						zc=0;
						goto step6;
					} 
					zc=0;

				}
				goto step4;
			}
		}
	else if(n[k]=='1')
		{
			if(!( (k==0)&&(j==1) )) //不是最左邊且不是十位
				{
					memcpy(o,"一",2);				
					o=o+2;
					*o=0;
				}
				
		}
	else if(n[k]=='2')
		{
			if(((k==0)&&(j==0))||(j>=2))
			{
				memcpy(o,"兩",2);				
				o=o+2;
				*o=0;
			
			}
			else
			{
				memcpy(o,"二",2);				
				o=o+2;
				*o=0;
			
			}
		}
	else
		{
			memcpy(o,&digit[ (n[k] -'0')*2 ],2);
			o=o+2;
			*o=0;
		}
step6:
		//step6
		if(k==m)
			goto step7;

		if(j==0 && zc<4)
			{
				r=(m-k)/4;
				if(r==1)
					{
					memcpy(o,"萬",2);				
					o=o+2;
					*o=0;
					}
				if(r==2)
					{
					memcpy(o,"億",2);				
					o=o+2;
					*o=0;
					}
				if(r==3)
					{
					memcpy(o,"兆",2);				
					o=o+2;
					*o=0;
					}
				
			}
			if(j==1)
				{
					memcpy(o,"十",2);				
					o=o+2;
					*o=0;
				}
			if(j==2)
				{
					memcpy(o,"百",2);				
					o=o+2;
					*o=0;
				}
			if(j==3)
				{
					memcpy(o,"千",2);				
					o=o+2;
					*o=0;
				}	
			if(n[k]!='0')
				zc=0;
			k=k+1;
			goto step4;

step7:   ;
			//printf("%s",o);			
		 return o;
}
